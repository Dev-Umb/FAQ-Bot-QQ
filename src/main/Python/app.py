import io

import numpy as np
from PIL import Image
from sanic import Sanic
from sanic.response import file_stream, raw, file, stream
from DTO.Pic import DBSession, Pic

app = Sanic("STapi")
url = "http://127.0.0.1:8000/"


def img_ege_get(byte: bytes):
    byte_stream = io.BytesIO(byte)
    im = Image.open(byte_stream).convert('L')
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
    imgByteArr = io.BytesIO()
    im2.save(imgByteArr, format='png')
    imgByteArr = imgByteArr.getvalue()
    return imgByteArr

# def img_ege_cv(image):


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
