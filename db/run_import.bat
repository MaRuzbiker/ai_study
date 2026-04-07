@echo off
chcp 65001 >nul
set MYSQL=mysql -uroot -p20040821 aistudy --default-character-set=utf8

echo Importing vocabulary data...

for %%f in (D:\QClaw-code\ai_study\db\book_5_*.sql) do (
    echo Importing %%~nxf...
    %MYSQL% < "%%f"
)
echo Done with book 5.

for %%f in (D:\QClaw-code\ai_study\db\book_6_*.sql) do (
    echo Importing %%~nxf...
    %MYSQL% < "%%f"
)
echo Done with book 6.

for %%f in (D:\QClaw-code\ai_study\db\book_7_*.sql) do (
    echo Importing %%~nxf...
    %MYSQL% < "%%f"
)
echo Done with book 7.

for %%f in (D:\QClaw-code\ai_study\db\book_8_*.sql) do (
    echo Importing %%~nxf...
    %MYSQL% < "%%f"
)
echo Done with book 8.

for %%f in (D:\QClaw-code\ai_study\db\book_9_*.sql) do (
    echo Importing %%~nxf...
    %MYSQL% < "%%f"
)
echo Done with book 9.

for %%f in (D:\QClaw-code\ai_study\db\book_10_*.sql) do (
    echo Importing %%~nxf...
    %MYSQL% < "%%f"
)
echo Done with book 10.

for %%f in (D:\QClaw-code\ai_study\db\book_11_*.sql) do (
    echo Importing %%~nxf...
    %MYSQL% < "%%f"
)
echo Done with book 11.

echo.
echo Verifying...
%MYSQL% -e "SELECT id, name, word_count FROM word_book ORDER BY id;"

echo All done!
pause
