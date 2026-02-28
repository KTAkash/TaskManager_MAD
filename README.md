Task Manager is a simple and user-friendly Android application that allows users to create, manage, edit, and delete daily tasks.
The app provides an intuitive interface to help users stay organized and track completed tasks easily.

#Features
1.Add new tasks with title and optional description
2.Mark tasks as completed
3.Edit existing tasks
4.Delete tasks
5.Persistent storage using SharedPreferences
6.Clean Material Design UI

<img width="800" height="1000" alt="Screenshot_20260228_153925" src="https://github.com/user-attachments/assets/eaa4ee27-8175-4025-bce6-12107d110c64" />

<img width="400" height="827" alt="Screenshot 2026-02-28 153701" src="https://github.com/user-attachments/assets/38ffd7e9-4d05-418f-9f57-9db6a5c3f098" />

<img width="800" height="1000" alt="Screenshot_20260228_153903" src="https://github.com/user-attachments/assets/21f94102-f348-4ade-a995-b75fed807167" />

Design Choices
1️) Material Design Principles
The app follows Material Design guidelines:
Purple primary color for consistency
Floating Action Button (FAB) for adding tasks
Card-based layout for task items
Clear visual indicators for completed tasks (strikethrough text)

2️) Simple & Minimal UI
Clean layout with sufficient spacing
Rounded cards for modern look
Disabled "Save Task" button until valid input is provided (prevents empty tasks)

3️)Data Persistence Strategy
Tasks are stored locally using SharedPreferences
Data is serialized into JSON using Gson
Exception handling ensures safe fallback in case of corrupted data

4️)User Experience (UX) Considerations
Instant visual feedback when marking tasks complete
Easy navigation between screens
Minimal clicks to perform actions
Clear button icons for edit and delete actions



