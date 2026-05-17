import os
import re

files_to_fix = [
    'Keys.java',
    'NetworksItemGroups.java',
    'NetworkSlimefunItems.java',
    'StackUtils.java',
    'NetworkStorage.java',
    'SupportedRecipes.java'
]

def de_lombok(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Remove Lombok imports
    content = re.sub(r'import lombok\..*?;\n', '', content)
    
    # Remove @UtilityClass and add static/private constructor
    if '@UtilityClass' in content:
        content = content.replace('@UtilityClass\n', '')
        # Add static to methods and fields (simplified approach)
        lines = content.split('\n')
        new_lines = []
        class_name = os.path.basename(file_path).split('.')[0]
        in_class = False
        
        for line in lines:
            if 'class ' in line or 'interface ' in line or 'enum ' in line:
                in_class = True
                new_lines.append(line)
                new_lines.append(f"    private {class_name}() {{}}")
                continue
                
            if not in_class:
                new_lines.append(line)
                continue
                
            match = re.match(r'^(\s+)(public|private|protected)\s+(?!static\b|class\b|enum\b)(.*)$', line)
            if match:
                rest = match.group(3)
                if not rest.startswith(f"{class_name}("): # Not constructor
                    new_line = f"{match.group(1)}{match.group(2)} static {match.group(3)}"
                    new_lines.append(new_line)
                else:
                    new_lines.append(line)
            else:
                new_lines.append(line)
        content = '\n'.join(new_lines)
    
    if '@Getter' in content or '@Setter' in content:
        content = content.replace('@Getter\n', '').replace('@Setter\n', '').replace('@Getter', '').replace('@Setter', '')
        # Add simple getters/setters if it's NetworkStorage
        if file_path.endswith('NetworkStorage.java'):
            getters = """
    public Location getLocation() { return location; }
    public ItemStack getItemStack() { return itemStack; }
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }
    public void setItemStack(ItemStack itemStack) { this.itemStack = itemStack; }
"""
            content = content.replace('}', getters + '}')

    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)

for root, dirs, files in os.walk('D:\\Documents\\GitHub\\slimefun\\Networks\\src'):
    for file in files:
        if file in files_to_fix:
            de_lombok(os.path.join(root, file))
            print(f"De-lomboked {file}")
