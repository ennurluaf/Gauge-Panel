import os
import json
from pathlib import Path

def generate_items_from_textures(textures_dir: str, output_path: str):
    textures_path = Path(textures_dir)
    items = []

    for file in textures_path.glob("*.png"):
        filename = file.name
        item_id = filename.replace(".png", "")
        item_name = item_id.replace("_", " ").title()

        items.append({
            "name": item_name,
            "id": f"create:{item_id}",
            "image": filename
        })

    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(items, f, indent=2)
    print(f"Written {len(items)} items to {output_path}")


if __name__ == "__main__":
    generate_items_from_textures(
        textures_dir="gaugepanel/src/main/resources/textures",
        output_path="gaugepanel/src/main/resources/items.json"
    )
