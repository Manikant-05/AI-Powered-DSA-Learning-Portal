# Reset Instructions After Code Updates

## Important: Follow These Steps After Code Updates

### Step 1: Reset the Database (Required for New Problems)

The new problems will only be created if the database is empty. Run this command:

**Option A: Using PowerShell Script (Recommended)**
```powershell
.\reset-database.ps1
```

**Option B: Manual Reset**
```powershell
# Stop containers
docker-compose -f docker-compose.yml down

# Remove database volume (this deletes all data)
docker volume rm ai-powereddsalearningportal_postgres_data

# Start containers again
docker-compose -f docker-compose.yml up -d

# Wait for services to start (about 20 seconds)
Start-Sleep -Seconds 20
```

### Step 2: Clear Browser Cache and LocalStorage (Required for Navigation Fix)

**In Chrome/Edge:**
1. Open Developer Tools (Press F12)
2. Go to **Application** tab
3. Click **Local Storage** → `http://localhost:3000`
4. Right-click and select **Clear**
5. Go to **Storage** tab → Click **Clear site data**
6. Close Developer Tools
7. **Hard refresh** the page (Ctrl + Shift + R or Ctrl + F5)

**Or use Browser Console:**
1. Press F12 to open Developer Tools
2. Go to **Console** tab
3. Type: `localStorage.clear(); location.reload();`
4. Press Enter

### Step 3: Verify Services Are Running

```powershell
docker-compose -f docker-compose.yml ps
```

All three services should show "Up" status:
- dsa_portal_postgres
- dsa_portal_backend  
- dsa_portal_frontend

### Step 4: Test the Application

1. Open browser and go to: http://localhost:3000
2. **Clear localStorage first** (use console method above if not done)
3. Login with:
   - Username: `admin` / Password: `admin`
   - OR Username: `testuser` / Password: `password123`
4. Test navigation:
   - Click "Dashboard" - should work
   - Click "Problems" - should work and show 22 problems
   - Click "Profile" - should work
   - Navigate back and forth - should all work

## Troubleshooting

### If navigation still doesn't work:
1. Make sure you cleared localStorage (Step 2)
2. Try a different browser or incognito mode
3. Check browser console for errors (F12 → Console)
4. Restart containers: `docker-compose -f docker-compose.yml restart`

### If problems don't show:
1. Make sure you reset the database (Step 1)
2. Check backend logs: `docker-compose -f docker-compose.yml logs backend`
3. Look for "Sample problems created" message

### If login doesn't work:
1. Clear localStorage (Step 2)
2. Try logging in again
3. Check backend logs for errors

## Quick Reset Command (All-in-One)

Run this in PowerShell from the project directory:

```powershell
docker-compose -f docker-compose.yml down; docker volume rm ai-powereddsalearningportal_postgres_data; docker-compose -f docker-compose.yml up -d; Start-Sleep -Seconds 20; Write-Host "Reset complete! Now clear your browser cache and localStorage." -ForegroundColor Green
```

Then clear browser cache as described in Step 2.

