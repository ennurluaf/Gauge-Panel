import zipfile
import json
import os
from pathlib import Path
from PIL import Image

def extract_items(create_jar_path: str, output_dir: str = "gaugepanel/src/main/resources") -> list:
    output_dir = Path(output_dir)
    items = []

    with zipfile.ZipFile(create_jar_path, 'r') as jar:
        jar_files = jar.namelist()

        item_jsons = [f for f in jar_files if f.startswith("assets/create/models/item") and f.endswith(".json")]
        texture_paths = set()

        for json_path in item_jsons:
            with jar.open(json_path) as f:
                model_data = json.load(f)
                item_id = Path(json_path).stem
                texture = model_data.get("textures", {}).get("layer0") or model_data.get("textures", {}).get("particle")

                if texture and texture.startswith("create:"):
                    relative_path = texture[7:] + '.png'
                    texture_path = f'assets/create/textures/{relative_path}'
                    image_name = os.path.basename(relative_path)
                else:
                    texture_path = f'assets/create/textures/{item_id}.png'
                    image_name = f'{item_id}.png'

                items.append({
                    "name": item_id.replace("_"," ").title(),
                    "id": f'create:{item_id}',
                    "image": image_name 
                })

                texture_paths.add(texture_path)

        texture_output = output_dir / "textures"
        texture_output.mkdir(parents = True, exist_ok = True)

        for tex in texture_paths:
            if tex in jar_files:
                dest_path = texture_output / os.path.basename(tex)
                with jar.open(tex) as img_file, open(dest_path, 'wb') as out_file:
                    out_file.write(img_file.read())

        with open(output_dir / "items.json", 'w') as json_file:
            json.dump(items, json_file, indent=2)
    
    return items


if __name__ == '__main__':
    create_jar_path = "C:/Users/Ennur/curseforge/minecraft/Downloads/create-1.21.1-6.0.6.jar"
    items = extract_items(create_jar_path)
    print(f'Extracted {len(items)} items to output/items.json')