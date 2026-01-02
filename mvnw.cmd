\
@echo off
where mvn >nul 2>&1
if errorlevel 1 (
  echo Erreur: Maven (mvn) introuvable.
  echo Installe Maven puis relance.
  exit /b 1
)
mvn %*
