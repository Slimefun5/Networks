import os
import re

file_path = 'D:\\Documents\\GitHub\\slimefun\\Networks\\src\\main\\java\\io\\github\\sefiraat\\networks\\slimefun\\NetworkSlimefunItems.java'

with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# The pattern is usually something like:
# NetworksItemGroups.GROUP, NetworksSlimefunItemStacks.ITEM.item(),
# We want to remove the .item() from the second argument

pattern = r'(NetworksItemGroups\.[A-Z0-9_]+,\s*\n?\s*)(NetworksSlimefunItemStacks\.[A-Z0-9_]+)\.item\(\)'
content = re.sub(pattern, r'\1\2', content)

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)
