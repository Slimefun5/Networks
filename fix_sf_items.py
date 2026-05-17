import os
import re

def fix_slimefun_items(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # We need to replace SlimefunItems.XXX and NetworksSlimefunItemStacks.XXX
    # where XXX is some constant name, with .item() appended.
    # We should only append .item() if it doesn't already have it, and if it's not being called a method on it.
    
    # Regex to find SlimefunItems.CONSTANT_NAME or NetworksSlimefunItemStacks.CONSTANT_NAME
    # Not followed by .item(), .getItem(), .clone(), etc.
    
    pattern = r'\b(SlimefunItems|NetworksSlimefunItemStacks)\.([A-Z0-9_]+)(?!\.\w+\()'
    
    # Replace with \1.\2.item()
    content = re.sub(pattern, r'\1.\2.item()', content)
    
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)

fix_slimefun_items('D:\\Documents\\GitHub\\slimefun\\Networks\\src\\main\\java\\io\\github\\sefiraat\\networks\\slimefun\\NetworkSlimefunItems.java')
