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

    def print_tree(self):
        self._print(self.root, 0)

    def _print(self, node, level):
        if node is None:
            return
        self._print(node.right, level + 1)
        print("    " * level + str(node.val))
        self._print(node.left, level + 1)

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


if __name__ == "__main__":
    tree = MyTree()

    s = input("Введите дерево (например, A(B(D,E),C(F,G))):\n")
    tree.build(s)

    print("\n---- Исходное дерево ----")
    tree.print_tree()

    tree.remove_non_bottom_leaves()

    print("\n---- После удаления листьев не на нижнем уровне ----")
    tree.print_tree()