@echo off
echo.
echo.
echo ===================================================
echo Tagging with version %1% and message %2%
@echo on
git tag -a %1% -m %2%
@echo off
echo.
echo.
echo Done.
echo ===================================================
echo.





