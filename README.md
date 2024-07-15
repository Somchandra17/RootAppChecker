# Root Detection App

This Android application is designed to detect whether a device is rooted. It utilizes various checks including file paths, system properties, and native code execution to determine the root status of the device.

## Features

- Check for known root files
- Detect SU binary presence
- Search for BusyBox installation
- Identify root management apps
- Verify system integrity
- Detect Magisk hide techniques
- Native checks for rooted paths and binaries

## Installation

To build and run the app, follow these steps:

1. Clone the repository:
```bash
  git clone https://github.com/Somchandra17/RootAppChecker
```
2. Open the project in Android Studio.
3. Build the project by navigating to `Build > Make Project`.
4. Run the app on a physical device or an emulator.

## Usage

Upon launching, the app presents a user interface with multiple checkboxes corresponding to different root checks. Press the "Check Root" button to perform the checks. The app will display whether the device is rooted based on the checks and show a detailed alert message.![UI](https://github.com/user-attachments/assets/4454fff5-0607-4a47-b157-ef1d1417ba5d)
![rootdetected](https://github.com/user-attachments/assets/8b21824c-3e57-4369-bb01-2159e0e92346)




## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues to suggest improvements or add new features.

