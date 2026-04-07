@echo off
chcp 65001 >nul
echo Importing vocabulary data...
echo.

echo Step 1: Delete old data...
mysql -uroot -p20040821 aistudy --default-character-set=utf8 -e "DELETE FROM word WHERE book_id >= 5; DELETE FROM word_book WHERE id >= 5;"

echo.
echo Step 2: Import new data (this may take a few minutes)...
mysql -uroot -p20040821 aistudy --default-character-set=utf8 < "D:\QClaw-code\ai_study\db\import_all_books.sql"

echo.
echo Step 3: Update word counts...
mysql -uroot -p20040821 aistudy --default-character-set=utf8 -e "UPDATE word_book SET word_count = (SELECT COUNT(*) FROM word WHERE word.book_id = word_book.id);"

echo.
echo Done! Verifying...
mysql -uroot -p20040821 aistudy --default-character-set=utf8 -e "SELECT id, name, word_count FROM word_book ORDER BY id;"

pause
