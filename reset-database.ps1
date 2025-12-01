# Reset Database Script for AI DSA Portal
# This script will reset the database to allow new problems to be created

Write-Host "Stopping containers..." -ForegroundColor Yellow
docker-compose -f docker-compose.yml down

Write-Host "Removing database volume..." -ForegroundColor Yellow
docker volume rm ai-powereddsalearningportal_postgres_data

Write-Host "Starting containers..." -ForegroundColor Yellow
docker-compose -f docker-compose.yml up -d

Write-Host "Waiting for services to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 20

Write-Host "Checking backend logs..." -ForegroundColor Yellow
docker-compose -f docker-compose.yml logs backend --tail=20

Write-Host "`nDatabase reset complete!" -ForegroundColor Green
Write-Host "The application should now have all 22 problems." -ForegroundColor Green
Write-Host "Please clear your browser cache and localStorage before accessing the application." -ForegroundColor Cyan

