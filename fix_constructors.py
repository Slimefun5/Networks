import os
import re

files_to_fix = [
    'Keys.java',
    'NetworksItemGroups.java',
    'NetworkSlimefunItems.java',
    'StackUtils.java',
    'NetworkStorage.java',
    'SupportedRecipes.java',
    'DataTypeMethods.java'
]

def remove_static_from_constructors(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    lines = content.split('\n')
    new_lines = []
    
    class_name = os.path.basename(file_path).split('.')[0]
    
    for line in lines:
        if f" static {class_name}(" in line:
            new_lines.append(line.replace(f" static {class_name}(", f" {class_name}("))
        else:
            new_lines.append(line)
            
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write('\n'.join(new_lines))

for root, dirs, files in os.walk('D:\\Documents\\GitHub\\slimefun\\Networks\\src'):
    for file in files:
        if file in files_to_fix:
            remove_static_from_constructors(os.path.join(root, file))
            print(f"Fixed {file}")
