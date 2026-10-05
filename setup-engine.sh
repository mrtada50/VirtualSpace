#!/data/data/com.termux/files/usr/bin/bash
# اختياري: لتجهيز المحرك محلياً (GitHub Actions يسويها تلقائياً)
set -e
git clone --depth 1 https://github.com/ALEX5402/NewBlackbox engine-src
BC=$(find engine-src -maxdepth 1 -iname bcore | head -n1)
rm -rf Bcore && cp -r "$BC" Bcore
echo "Done: Bcore ready"
