import zipfile
from PIL import Image
import os
from pathlib import Path 
import shutil

def creep_resize(filename, img, size=80, offset=0):
    img_w, img_h = img.size

    if ("creep1_3_attack_" in filename):
        res = Image.new("RGBA", (size+20, size+20), (255, 255, 255, 0))
        res.paste(img, (offset+10, size-img_h+10))
    else:
        res = Image.new("RGBA", (size, size), (255, 255, 255, 0))
        res.paste(img, (offset, size-img_h))
    return res

def super_resize(filename, img, size):
    img_w, img_h = img.size

    res = Image.new("RGBA", (size, size), (255, 255, 255, 0))
    if ("super1_1" in filename):
        res.paste(img, (18, 36))
    elif ("super1_2" in filename):
        res.paste(img, (13, 38))
    elif ("super1_3" in filename):
        res.paste(img, (11, 43))
    elif ("super1_4" in filename):
        res.paste(img, (13, 38))

    elif ("super5" in filename):
        res.paste(img, (16, 44))

    elif ("creep5_3_death_1" in filename):
        res.paste(img, (20, 59))
    elif ("creep5_3_death_2" in filename):
        res.paste(img, (20, 58))
    elif ("creep5_3_death_3" in filename):
        res.paste(img, (6, 54))
    elif ("creep5_3_death_4" in filename):
        res.paste(img, (20, 60))
    elif ("creep5_3_death_5" in filename):
        res.paste(img, (20, 74))

    elif ("gun1_2_1" in filename):
        res.paste(img, (16, 23))
    elif ("gun1_2_2" in filename):
        res.paste(img, (16, 23))
    elif ("gun1_2_3" in filename):
        res.paste(img, (16, 19))
    elif ("gun1_2_4" in filename):
        res.paste(img, (16, 15))
    elif ("gun1_2_5" in filename):
        res.paste(img, (16, 15))
    elif ("gun1_2_6" in filename):
        res.paste(img, (16, 19))
    elif ("gun1_2_7" in filename):
        res.paste(img, (16, 23))
    elif ("gun1_2_8" in filename):
        res.paste(img, (16, 23))

    elif ("gun2_2_1" in filename):
        res.paste(img, (12, 20))
    elif ("gun2_2_2" in filename):
        res.paste(img, (12, 14))
    elif ("gun2_2_3" in filename):
        res.paste(img, (12, 7))
    elif ("gun2_2_4" in filename):
        res.paste(img, (12, 16))
    elif ("gun2_2_5" in filename):
        res.paste(img, (12, 20))

    elif ("gun2_4_1" in filename):
        res.paste(img, (12, 20))
    elif ("gun2_4_2" in filename):
        res.paste(img, (12, 12))
    elif ("gun2_4_3" in filename):
        res.paste(img, (12, 7))
    elif ("gun2_4_4" in filename):
        res.paste(img, (12, 16))
    elif ("gun2_4_5" in filename):
        res.paste(img, (12, 20))

    elif ("gun3_2_1" in filename):
        res.paste(img, (15, 23))
    elif ("gun3_2_2" in filename):
        res.paste(img, (15, 23))
    elif ("gun3_2_3" in filename):
        res.paste(img, (13, 21))
    elif ("gun3_2_4" in filename):
        res.paste(img, (15, 23))

    elif ("gun3_4_1" in filename):
        res.paste(img, (15, 23))
    elif ("gun3_4_2" in filename):
        res.paste(img, (15, 23))
    elif ("gun3_4_3" in filename):
        res.paste(img, (13, 23))
    elif ("gun3_4_4" in filename):
        res.paste(img, (15, 23))
    elif ("gun3_4_5" in filename):
        res.paste(img, (13, 21))
    elif ("gun3_4_6" in filename):
        res.paste(img, (15, 23))

    elif ("gun4_2_1" in filename):
        res.paste(img, (20, 20))
    elif ("gun4_2_2" in filename):
        res.paste(img, (19, 20))

    elif ("gun4_3_" in filename):
        res.paste(img, (20, 20))

    elif ("gun4_4_" in filename):
        res.paste(img, (20, 20))

    elif ("gun5_1_" in filename):
        res.paste(img, (20, 23))

    elif ("gun5_2_" in filename):
        res.paste(img, (20, 24))

    else:
        res.paste(img, (0, 0))
    return res

offset = 0
creep_size=80
last = ""

def makedirs(row1):
    path = f"{'/'.join(row1.split('/')[:-1])}"
    if not os.path.exists(path):
        os.makedirs(path)

with open('dict.csv', 'r', encoding='utf-8-sig') as file:
    for line in file:
        row = line.strip().split(';')

        makedirs(row[1])
        print(row[1])

        if (".wav" in row[0] or ".mp3" in row[0]):
            shutil.copy(Path(f'tmp/res/raw/{row[0]}'), Path(row[1]))
            continue

        img = Image.open(f'tmp/res/drawable/{row[0]}')

        res = ""

        if (row[2]=='1'):
            cur = row[1].split('.')[:-1][0][:-1]
            if (cur!=last):
                last=cur
                img_w, img_h = img.size
                offset = int((creep_size-img_w)/2)

            res = creep_resize(row[1], img, offset=offset)
        elif (row[2]=='2'):
            img_w, img_h = img.size
            res = creep_resize(row[1], img, offset = int((creep_size-img_w)/2))
        elif (row[2]=='3'):
            res = super_resize(row[1], img, size = 120)
        elif (row[2]=='4'):
            res = super_resize(row[1], img, size = 60)
        else:
            res = img
        res.save(f"{row[1]}")

