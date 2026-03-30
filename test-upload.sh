#!/bin/bash

echo "🧪 开始测试文件上传模块..."
echo ""

BASE_URL="http://localhost:8081"

# 等待服务启动
echo "⏳ 检查服务是否运行..."
if ! curl -s "$BASE_URL/search/popular" > /dev/null; then
    echo "❌ 服务未运行！请先启动 Spring Boot 应用"
    exit 1
fi
echo "✅ 服务运行正常"
echo ""

# 创建测试文件
echo "📝 创建测试文件..."
mkdir -p ./test-uploads

# 创建一个测试图片（1x1 像素的 PNG）
echo 'iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==' | base64 -d > ./test-uploads/test-image.png

# 创建一个测试文档
echo "这是一个测试文档" > ./test-uploads/test-document.txt

echo "✅ 测试文件已创建"
echo ""

echo "=========================================="
echo "📤 测试 1: 上传图片"
echo "=========================================="
curl -X POST "$BASE_URL/upload/image" \
  -F "file=@./test-uploads/test-image.png" \
  -H "Accept: application/json" | jq '.'

echo ""
echo "=========================================="
echo "📤 测试 2: 上传文档"
echo "=========================================="
curl -X POST "$BASE_URL/upload/document" \
  -F "file=@./test-uploads/test-document.txt" \
  -H "Accept: application/json" | jq '.'

echo ""
echo "=========================================="
echo "📤 测试 3: 上传任意文件"
echo "=========================================="
curl -X POST "$BASE_URL/upload/file" \
  -F "file=@./test-uploads/test-image.png" \
  -H "Accept: application/json" | jq '.'

echo ""
echo "=========================================="
echo "📤 测试 4: 批量上传图片"
echo "=========================================="
curl -X POST "$BASE_URL/upload/images" \
  -F "files=@./test-uploads/test-image.png" \
  -F "files=@./test-uploads/test-image.png" \
  -H "Accept: application/json" | jq '.'

echo ""
echo "=========================================="
echo "✅ 所有测试完成！"
echo "=========================================="
echo ""
echo "📂 测试文件位置：./test-uploads/"
echo ""
echo "💡 提示："
echo "  1. 查看上传的文件：ls -la uploads/images/"
echo "  2. 在浏览器中访问上传的图片："
echo "     http://localhost:8081/uploads/images/xxx.png"
echo ""

# 清理测试文件（可选）
# rm -rf ./test-uploads/
