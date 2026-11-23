# Power Master Pro V3

Target device: OPPO F19 Pro+ (CPH2213), Android 13 / ColorOS 13.

## Features
- User-started foreground motion service for accelerometer shake detection.
- Attempts to wake the display after a shake.
- Optional Device Admin permission for screen lock.
- Accessibility key filtering for Volume Up to lock the screen.

## Build
Run the GitHub Actions workflow **Build Power Master Pro APK** from the Actions tab. Download the `PowerMasterProV3-debug` artifact and extract `app-debug.apk`.

## First launch setup
1. Install the debug APK.
2. Open the app and grant notification permission.
3. Tap **Enable Screen Lock Permission** and approve Device Admin.
4. Tap **Enable Volume Up Accessibility** and enable Power Master Pro in Accessibility settings.
5. Tap **Start Shake-to-Wake Service**.
6. In OPPO battery settings, set app battery use to unrestricted / allow background activity if the option exists.

## Important limitations
Android and OPPO firmware may suspend or batch accelerometer events while the display is off. A foreground service and partial wake lock improve the chance of receiving events but cannot guarantee shake-to-wake on every firmware build. This app uses additional battery. The deprecated screen wake-lock API is used as a best-effort wake attempt; behavior must be tested on the actual handset.

This is a debug build for personal testing, not a Play Store release. It is not signed with a private release key.
