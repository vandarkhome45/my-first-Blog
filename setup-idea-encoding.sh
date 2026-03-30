#!/bin/bash

echo "🔧 正在配置 IDEA 的文件编码设置..."
echo ""

# 创建 IDEA 配置文件目录
mkdir -p .idea

# 创建编码配置文件
cat > .idea/encodings.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="Encoding" defaultCharsetForPropertiesFiles="UTF-8">
    <file url="file://$PROJECT_DIR$/src/main/java" charset="UTF-8" />
    <file url="file://$PROJECT_DIR$/src/main/resources" charset="UTF-8" />
    <file url="file://$PROJECT_DIR$/src/test/java" charset="UTF-8" />
    <file url="file://$PROJECT_DIR$/src/test/resources" charset="UTF-8" />
    <file url="file://$PROJECT_DIR$" charset="UTF-8" />
  </component>
</project>
EOF

echo "✅ 已创建 .idea/encodings.xml"
echo ""

# 创建 misc.xml 配置文件
cat > .idea/misc.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="ProjectRootManager" version="2" languageLevel="JDK_25" default="true" project-jdk-name="25" project-jdk-type="JavaSDK">
    <output url="file://$PROJECT_DIR$/target/classes" />
  </component>
</project>
EOF

echo "✅ 已创建 .idea/misc.xml"
echo ""

echo "📝 接下来请在 IDEA 中手动设置："
echo ""
echo "1. 打开 IDEA 设置：Cmd + ,"
echo "2. 导航到：Editor → File Encodings"
echo "3. 设置以下三项为 UTF-8："
echo "   - Global Encoding: UTF-8"
echo "   - Project Encoding: UTF-8"  
echo "   - Default encoding for properties files: UTF-8"
echo "4. 勾选：Transparent native-to-ascii conversion for properties files"
echo "5. 点击 Apply 和 OK"
echo ""
echo "完成以上步骤后，properties 文件就可以正常使用中文注释了！"
