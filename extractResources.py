import zipfile
from PIL import Image
import os
from pathlib import Path 

def creep_resize(img, size=80, offset=0):
    # img = Image.open(srcimg)
    img_w, img_h = img.size

    res = Image.new("RGBA", (size, size), (255, 255, 255, 0))
    res.paste(img, (offset, size-img_h))
    return res
    # res.save(name)

offset = 0
creep_size=80
last = ""

with open('dict.csv', 'r', encoding='utf-8-sig') as file:
    for line in file:
        row = line.strip().split(';')
        img = Image.open(f'tmp/res/drawable/{row[0]}')

        path = f"{'/'.join(row[1].split('/')[:-1])}"
        if not os.path.exists(path):
            os.makedirs(path)

        res = ""

        print(row[1])
        if (row[2]=='1'):
            cur = row[1].split('.')[:-1][0][:-1]
            if (cur!=last):
                last=cur
                img_w, img_h = img.size
                offset = int((creep_size-img_w)/2)

            res = creep_resize(img, offset=offset)
        elif (row[2]=='2'):
            img_w, img_h = img.size
            res = creep_resize(img, offset = int((creep_size-img_w)/2))
        else:
            res = img
        res.save(f"{row[1]}")


