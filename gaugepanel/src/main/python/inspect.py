import tkinter as tk
from tkinter import filedialog, ttk, messagebox
import zipfile
import os

class JarExplorer(tk.Tk):
    def __init__(self, jar_path=None):
        super().__init__()
        self.title("Inspect .jar / Directory")
        self.geometry("800x600")

        self.tree = ttk.Treeview(self)
        self.tree.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)

        self.scrollbar = ttk.Scrollbar(self, orient="vertical", command=self.tree.yview)
        self.tree.configure(yscrollcommand=self.scrollbar.set)
        self.scrollbar.pack(side=tk.LEFT, fill=tk.Y)

        self.preview = tk.Text(self, wrap="word")
        self.preview.pack(side=tk.RIGHT, fill=tk.BOTH, expand=True)

        self.tree.bind("<<TreeviewSelect>>", self.preview_file)

        # Menu
        menubar = tk.Menu(self)
        filemenu = tk.Menu(menubar, tearoff=0)
        filemenu.add_command(label="Open JAR/Directory", command=self.open_file)
        filemenu.add_separator()
        filemenu.add_command(label="Exit", command=self.quit)
        menubar.add_cascade(label="File", menu=filemenu)
        self.config(menu=menubar)

        if jar_path:
            self.load_jar(jar_path)

    def open_file(self):
        path = filedialog.askopenfilename(filetypes=[("JAR or ZIP", "*.jar *.zip"), ("All Files", "*.*")])
        if path:
            self.load_jar(path)

    def load_jar(self, path):
        self.tree.delete(*self.tree.get_children())
        self.preview.delete("1.0", tk.END)

        if os.path.isdir(path):
            self.build_tree_from_dir(path)
        else:
            try:
                self.zip = zipfile.ZipFile(path, 'r')
                self.build_tree_from_zip()
            except zipfile.BadZipFile:
                messagebox.showerror("Error", "Selected file is not a valid JAR or ZIP.")
                return

    def build_tree_from_zip(self):
        root_node = self.tree.insert("", "end", text="(JAR Root)", open=True)
        nodes = {}

        for file in self.zip.namelist():
            parts = file.strip("/").split("/")
            current = root_node
            for i, part in enumerate(parts):
                path = "/".join(parts[:i + 1])
                if path not in nodes:
                    nodes[path] = self.tree.insert(current, "end", text=part, values=(path,))
                current = nodes[path]

    def build_tree_from_dir(self, dir_path):
        def walk_dir(parent, path):
            for entry in sorted(os.listdir(path)):
                full = os.path.join(path, entry)
                node = self.tree.insert(parent, "end", text=entry, values=(full,))
                if os.path.isdir(full):
                    walk_dir(node, full)

        root_node = self.tree.insert("", "end", text=os.path.basename(dir_path), open=True, values=(dir_path,))
        walk_dir(root_node, dir_path)

    def preview_file(self, event):
        selected = self.tree.focus()
        item = self.tree.item(selected)
        file_path = item["values"][0] if item["values"] else None

        self.preview.delete("1.0", tk.END)
        if not file_path:
            return

        if hasattr(self, "zip"):
            if file_path.endswith("/"): return
            try:
                with self.zip.open(file_path) as f:
                    content = f.read(5000).decode("utf-8", errors="ignore")
                    self.preview.insert(tk.END, content)
            except:
                self.preview.insert(tk.END, "[Binary or unreadable file]")
        else:
            if os.path.isdir(file_path): return
            try:
                with open(file_path, "r", encoding="utf-8") as f:
                    self.preview.insert(tk.END, f.read(5000))
            except:
                self.preview.insert(tk.END, "[Binary or unreadable file]")

if __name__ == "__main__":
    create_jar_path = "/home/joey/Downloads/create-1.20.1-6.0.4.jar"
    app = JarExplorer(jar_path=create_jar_path)
    app.mainloop()
