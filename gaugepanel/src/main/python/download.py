import os
import json
import requests
import re
from bs4 import BeautifulSoup
from urllib.parse import urljoin
from pathlib import Path

ROOT_URL = "https://www.minecraftinfo.com"
LIST_URL = urljoin(ROOT_URL, "idlist.htm")
HEADERS = {"User-Agent": "Mozilla/5.0"}

BASE_PATH = Path("gaugepanel/src/main/resources/textures/minecraft")
TEXTURE_DIR = BASE_PATH / "textures"
ITEMS_JSON_PATH = BASE_PATH / "items.json"

# Ensure directories exist
TEXTURE_DIR.mkdir(parents=True, exist_ok=True)
ITEMS_JSON_PATH.parent.mkdir(parents=True, exist_ok=True)

def clean_name(name: str) -> str:
    return re.sub(r'[^a-z0-9_]', '', name.strip().lower().replace(" ", "_"))

def get_item_info(page_url: str) -> dict | None:
    try:
        res = requests.get(page_url, headers=HEADERS)
        res.raise_for_status()
        soup = BeautifulSoup(res.text, "html.parser")

        # Extract ID
        id_td = soup.find("td", string="ID Name:")
        if not id_td:
            return None
        mc_id = id_td.find_next_sibling("td").get_text(strip=True)
        full_id = f"minecraft:{mc_id}"

        # Get raw name from header
        title_div = soup.select_one("div.bar span.title")
        if not title_div:
            return None
        name_text = title_div.get_text(strip=True).split("->")[-1].strip()

        # Extract image URL
        mimg_div = soup.select_one("div.mimg[style]")
        image_url = None
        if mimg_div:
            match = re.search(r"url\((.*?)\)", mimg_div["style"])
            if match:
                image_url = urljoin(ROOT_URL, match.group(1))

        if not image_url:
            return None

        image_name = f"{clean_name(name_text)}.png"
        image_path = TEXTURE_DIR / image_name

        # Download image if it doesn't exist
        if not image_path.exists():
            img_res = requests.get(image_url, headers=HEADERS)
            img_res.raise_for_status()
            with open(image_path, "wb") as f:
                f.write(img_res.content)

        return {
            "name": name_text,
            "id": full_id,
            "image": image_name
        }

    except Exception as e:
        print(f"❌ Error parsing {page_url}: {e}")
        return None

def scrape_all_items():
    print("🌐 Fetching main item list...")
    res = requests.get(LIST_URL, headers=HEADERS)
    res.raise_for_status()
    soup = BeautifulSoup(res.text, "html.parser")

    items = []
    tables = soup.find_all("table")[1:]  # Skip first (legend) table

    for table in tables:
        for row in table.find_all("tr")[1:]:  # Skip header row
            link = row.find("a", href=True)
            if not link or not link["href"].endswith(".htm"):
                continue

            page_url = urljoin(ROOT_URL, link["href"])
            print(f"🔍 {page_url}")
            item = get_item_info(page_url)

            if item:
                items.append(item)
                print(f"✅ {item['name']} ({item['id']})")
            else:
                print(f"⚠️ Skipped {page_url}")

    return items

def save_items_json(items):
    with open(ITEMS_JSON_PATH, "w", encoding="utf-8") as f:
        json.dump(items, f, indent=2, ensure_ascii=False)
    print(f"\n📦 Saved {len(items)} items to {ITEMS_JSON_PATH}")

if __name__ == "__main__":
    all_items = scrape_all_items()
    save_items_json(all_items)
