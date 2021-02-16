import io

import cv2
import numpy as np
from PIL import Image, ImageFilter
from sanic import Sanic
from sanic.response import file_stream, raw, file, stream

app = Sanic("STapi")
url = "http://127.0.0.1:8000/"


def img_ege_get(byte: bytes):
    byte_stream = io.BytesIO(byte)
    im = Image.open(byte_stream).convert('L')
    im = im.filter(ImageFilter.GaussianBlur(radius=1.5)) # 高斯滤镜
    a = np.asarray(im).astype('float')

    depth = 10.
    grad = np.gradient(a)
    grad_x, grad_y = grad
    grad_x = grad_x * depth / 100.
    grad_y = grad_y * depth / 100.
    A = np.sqrt(grad_x ** 2 + grad_y ** 2 + 1.)
    uni_x = grad_x / A
    uni_y = grad_y / A
    uni_z = 1. / A

    vec_el = np.pi / 2.2
    vec_az = np.pi / 4.
    dx = np.cos(vec_el) * np.cos(vec_az)
    dy = np.cos(vec_el) * np.sin(vec_az)
    dz = np.sin(vec_el)

    b = 255 * (dx * uni_x + dy * uni_y + dz * uni_z)
    b = b.clip(0, 255)
    im2 = Image.fromarray(b.astype('uint8'))
    im2 = im2.filter(ImageFilter.SHARPEN) # 锐化
    imgByteArray = io.BytesIO()
    im2.save(imgByteArray, format='png')
    imgByteArray = imgByteArray.getvalue()
    return imgByteArray

#canny算法
# def img_ege_canny(byte: bytes) -> bytes:
#     byte_stream = io.BytesIO(byte)
#     image = Image.open(byte_stream).convert('L')
#     img = cv2.cvtColor(np.asarray(image), cv2.IMREAD_GRAYSCALE)
#     img = cv2.GaussianBlur(img, (3, 3), 1.25)
#     canny = ~cv2.Canny(img, 20, 50)
#     im2 = Image.fromarray(canny.astype('uint8'))
#     im2 = im2.filter(ImageFilter.SHARPEN)
#     imgByteArray = io.BytesIO()
#     im2.save(imgByteArray, format='png')
#     imgByteArray = imgByteArray.getvalue()
#     return imgByteArray


@app.route("/img", methods=['GET', 'POST'])
async def test(request):
    try:
        pic = request.files['file'][0].body
    except:
        pic = request.body
    return raw(
        img_ege_get(pic), content_type='image/png'
    )


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=8000)
