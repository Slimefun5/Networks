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

def add_static(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Add static to public/private/protected fields and methods that don't have it
    # Be careful not to add it to class declarations or if it already has static
    
    lines = content.split('\n')
    new_lines = []
    
    in_class = False
    
    for line in lines:
        if 'class ' in line or 'interface ' in line or 'enum ' in line:
            in_class = True
            new_lines.append(line)
            continue
            
        if not in_class:
            new_lines.append(line)
            continue
            
        # Match lines that look like field or method declarations
        # e.g. public final NamespacedKey ...
        # e.g. public boolean testRecipe(...
        # Avoid things inside methods. But a simple regex on lines that start with spaces then public/private/protected
        
        match = re.match(r'^(\s+)(public|private|protected)\s+(?!static\b|class\b)(.*)$', line)
        if match:
            # Check if it's a constructor
            rest = match.group(3)
            # Not a constructor if it has a return type (space before '(' or no '(')
            # Rough heuristic: if it looks like a method or field, add static
            new_line = f"{match.group(1)}{match.group(2)} static {match.group(3)}"
            new_lines.append(new_line)
        else:
            new_lines.append(line)
            
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write('\n'.join(new_lines))

for root, dirs, files in os.walk('D:\\Documents\\GitHub\\slimefun\\Networks\\src'):
    for file in files:
        if file in files_to_fix:
            add_static(os.path.join(root, file))
            print(f"Fixed {file}")
