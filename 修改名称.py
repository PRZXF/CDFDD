from PIL import Image
import os

# 处理当前文件夹
folder_path = r"C:\Users\素华\Desktop\good新1\good"

for filename in os.listdir(folder_path):
    if filename.lower().endswith(".webp"):
        webp_path = os.path.join(folder_path, filename)
        
        # 打开 webp 并保存为 png
        with Image.open(webp_path) as img:
            # 新文件名
            png_name = os.path.splitext(filename)[0] + ".png"
            png_path = os.path.join(folder_path, png_name)
            
            # 保存
            img.save(png_path, "PNG")
            print(f"已转换：{filename} → {png_name}")
            