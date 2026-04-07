@echo off
chcp 65001 >nul
echo Deleting old data...
mysql -uroot -p20040821 aistudy --default-character-set=utf8 -e "DELETE FROM word WHERE book_id >= 5; DELETE FROM word_book WHERE id >= 5;"

echo Importing 初中词汇...
mysql -uroot -p20040821 aistudy --default-character-set=utf8 < "D:\QClaw-code\ai_study\db\book_5.sql"

echo Importing 高中词汇...
mysql -uroot -p20040821 aistudy --default-character-set=utf8 < "D:\QClaw-code\ai_study\db\book_6.sql"

echo Importing CET-4词汇...
mysql -uroot -p20040821 aistudy --default-character-set=utf8 < "D:\QClaw-code\ai_study\db\book_7.sql"

echo Importing CET-6词汇...
mysql -uroot -p20040821 aistudy --default-character-set=utf8 < "D:\QClaw-code\ai_study\db\book_8.sql"

echo Importing 考研词汇...
mysql -uroot -p20040821 aistudy --default-character-set=utf8 < "D:\QClaw-code\ai_study\db\book_9.sql"

echo Importing 托福词汇...
mysql -uroot -p20040821 aistudy --default-character-set=utf8 < "D:\QClaw-code\ai_study\db\book_10.sql"

echo Importing SAT词汇...
mysql -uroot -p20040821 aistudy --default-character-set=utf8 < "D:\QClaw-code\ai_study\db\book_11.sql"

echo.
echo Done! Verifying...
mysql -uroot -p20040821 aistudy --default-character-set=utf8 -e "SELECT id, name, word_count FROM word_book ORDER BY id;"
pause