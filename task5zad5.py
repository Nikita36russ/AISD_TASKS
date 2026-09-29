import tkinter as tk
from tkinter import scrolledtext, messagebox

class TreeNode:
    def __init__(self, val):
        self.val = val
        self.left = None
        self.right = None


class MyTree:
    def __init__(self):
        self.root = None

    def build(self, s):
        self.pos = 0
        self.root = self._parse(s)

    def _parse(self, s):
        if self.pos >= len(s) or s[self.pos] == ')':
            return None

        start = self.pos
        while self.pos < len(s) and s[self.pos] not in '(),':
            self.pos += 1
        node = TreeNode(s[start:self.pos].strip())

        if self.pos < len(s) and s[self.pos] == '(':
            self.pos += 1
            node.left = self._parse(s)
            if self.pos < len(s) and s[self.pos] == ',':
                self.pos += 1
            node.right = self._parse(s)
            if self.pos < len(s) and s[self.pos] == ')':
                self.pos += 1
        return node

    def get_tree_string(self):
        lines = []
        self._get_tree_string(self.root, 0, lines)
        return "\n".join(lines)

    def _get_tree_string(self, node, level, lines):
        if node is None:
            return
        self._get_tree_string(node.right, level + 1, lines)
        lines.append("    " * level + str(node.val))
        self._get_tree_string(node.left, level + 1, lines)

    def remove_non_bottom_leaves(self):
        max_depth = self._max_depth(self.root)
        self.root = self._delete_leaves(self.root, 1, max_depth)

    def _max_depth(self, node):
        if node is None:
            return 0
        return 1 + max(self._max_depth(node.left), self._max_depth(node.right))

    def _delete_leaves(self, node, depth, max_depth):
        if node is None:
            return None
        if node.left is None and node.right is None:
            return None if depth < max_depth else node
        node.left = self._delete_leaves(node.left, depth + 1, max_depth)
        node.right = self._delete_leaves(node.right, depth + 1, max_depth)
        return node


class TreeApp:
    def __init__(self, root_window):
        self.root_window = root_window
        self.root_window.title("Задача 5")
        self.root_window.geometry("600x500")
        
        self.tree = MyTree()

        input_frame = tk.Frame(root_window)
        input_frame.pack(pady=10, fill=tk.X, padx=10)
        
        tk.Label(input_frame, text="Введите дерево (дерево вида: A(B(D,E),C(F,G))):").pack(anchor=tk.W)
        self.entry = tk.Entry(input_frame, width=50)
        self.entry.pack(fill=tk.X, pady=5)

        button_frame = tk.Frame(root_window)
        button_frame.pack(pady=5)

        tk.Button(button_frame, text="Построить дерево", command=self.build_tree_action).pack(side=tk.LEFT, padx=5)
        tk.Button(button_frame, text="Удалить листья не на нижнем уровне", command=self.remove_leaves_action).pack(side=tk.LEFT, padx=5)

        tk.Label(root_window, text="Результат:").pack(anchor=tk.W, padx=10)
        self.text_area = scrolledtext.ScrolledText(root_window, width=60, height=20, font=("Courier New", 12))
        self.text_area.pack(padx=10, pady=5, fill=tk.BOTH, expand=True)

    def build_tree_action(self):
        tree_str = self.entry.get().strip()
        if not tree_str:
            messagebox.showwarning("Предупреждение", "Введите строку с деревом!")
            return
        
        try:
            self.tree.build(tree_str)
            self.text_area.delete(1.0, tk.END)
            self.text_area.insert(tk.END, "---- Исходное дерево ----\n")
            self.text_area.insert(tk.END, self.tree.get_tree_string())
        except Exception as e:
            messagebox.showerror("Ошибка", f"Ошибка при построении дерева: {e}")

    def remove_leaves_action(self):
        if self.tree.root is None:
            messagebox.showwarning("Предупреждение", "Сначала постройте дерево!")
            return
        
        self.tree.remove_non_bottom_leaves()
        self.text_area.insert(tk.END, "\n\n---- После удаления листьев не на нижнем уровне ----\n")
        self.text_area.insert(tk.END, self.tree.get_tree_string())


if __name__ == "__main__":
    root = tk.Tk()
    app = TreeApp(root)
    root.mainloop()