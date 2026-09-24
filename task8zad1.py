import tkinter as tk
from itertools import permutations

R = 25
PALETTE = ['#e6194B', '#3cb44b', '#4363d8', '#f58231', '#911eb4',
           '#42d4f4', '#f032e6', '#bfef45', '#fabed4', '#469990']


class GraphCanvas:
    """Один холст = один граф с своими вершинами, рёбрами и запретами."""
    def __init__(self, parent, title):
        self.frame = tk.Frame(parent)
        self.frame.pack(side=tk.LEFT, padx=5)

        tk.Label(self.frame, text=title, font=('Arial', 12, 'bold')).pack()

        btns = tk.Frame(self.frame)
        btns.pack(pady=2)
        tk.Button(btns, text="+ Вершина", command=lambda: self.set_mode('vertex')).pack(side=tk.LEFT)
        tk.Button(btns, text="-> Ребро", command=lambda: self.set_mode('edge')).pack(side=tk.LEFT)
        tk.Button(btns, text="X Запрет", command=lambda: self.set_mode('banned')).pack(side=tk.LEFT)
        tk.Button(btns, text="Очистить", command=self.clear).pack(side=tk.LEFT)

        self.mode_label = tk.Label(self.frame, text="Режим: просмотр", fg='blue')
        self.mode_label.pack()

        self.canvas = tk.Canvas(self.frame, width=480, height=480, bg='white')
        self.canvas.pack()
        self.canvas.bind("<Button-1>", self.on_click)

        self.vertices = {}
        self.edges = []
        self.banned = set()
        self.highlight = {}
        self.edge_first = None
        self.mode = 'view'

    def set_mode(self, mode):
        self.mode = mode
        self.edge_first = None
        self.mode_label.config(text=f"Режим: {mode}")
        self.draw()

    def on_click(self, event):
        x, y = event.x, event.y
        if self.mode == 'vertex':
            name = chr(ord('A') + len(self.vertices))
            if len(self.vertices) >= 26:
                name = f"V{len(self.vertices)}"
            self.vertices[name] = (x, y)
        elif self.mode == 'edge':
            v = self.find_vertex(x, y)
            if v is None:
                return
            if self.edge_first is None:
                self.edge_first = v
            else:
                if self.edge_first != v:
                    self.edges.append((self.edge_first, v))
                self.edge_first = None
        elif self.mode == 'banned':
            v = self.find_vertex(x, y)
            if v is None:
                return
            if v in self.banned:
                self.banned.remove(v)
            else:
                self.banned.add(v)
        self.draw()

    def find_vertex(self, x, y):
        for name, (vx, vy) in self.vertices.items():
            if (x - vx) ** 2 + (y - vy) ** 2 <= R * R:
                return name
        return None

    def clear(self):
        self.vertices = {}
        self.edges = []
        self.banned = set()
        self.highlight = {}
        self.edge_first = None
        self.draw()

    def draw(self):
        self.canvas.delete("all")

                                                     # ребра
                                                     
        for a, b in self.edges:
            x1, y1 = self.vertices[a]
            x2, y2 = self.vertices[b]
            dx, dy = x2 - x1, y2 - y1
            L = (dx * dx + dy * dy) ** 0.5
            if L == 0:
                continue
            ux, uy = dx / L, dy / L
            sx, sy = x1 + ux * R, y1 + uy * R
            ex, ey = x2 - ux * R, y2 - uy * R
            self.canvas.create_line(sx, sy, ex, ey, arrow=tk.LAST, width=1)

                                                      # вершины
                                                      
        for name, (x, y) in self.vertices.items():
            color = 'white'
            if name in self.banned:
                color = '#ff6666'
            if name in self.highlight:
                color = self.highlight[name]
            self.canvas.create_oval(x - R, y - R, x + R, y + R,
                                    fill=color, outline='black', width=2)
            self.canvas.create_text(x, y, text=name, font=('Arial', 12, 'bold'))

                                                  # подсветка вершин
                                                  
        if self.edge_first and self.edge_first in self.vertices:
            x, y = self.vertices[self.edge_first]
            self.canvas.create_oval(x - R - 5, y - R - 5, x + R + 5, y + R + 5,
                                    outline='blue', width=2)

    def get_matrix(self):
        """Возвращает матрицу смежности и упорядоченный список имён вершин."""
        names = sorted(self.vertices.keys())
        idx = {n: i for i, n in enumerate(names)}
        n = len(names)
        m = [[0] * n for _ in range(n)]
        for a, b in self.edges:
            if a in idx and b in idx:
                m[idx[a]][idx[b]] = 1
        return m, names

    def load(self, vertices, edges):
        self.clear()
        self.vertices = dict(vertices)
        self.edges = list(edges)
        self.draw()


def is_isomorphic(g1, g2):
    """Перебор перестановок. Возвращает список соответствий или None."""
    n = len(g1)
    if n != len(g2):
        return None
    for perm in permutations(range(n)):
        ok = True
        for i in range(n):
            for j in range(n):
                if g1[i][j] != g2[perm[i]][perm[j]]:
                    ok = False
                    break
            if not ok:
                break
        if ok:
            return perm
    return None


def main():
    root = tk.Tk()
    root.title("Задача 8 (вариант 1): Изоморфность графов")

    top = tk.Frame(root)
    top.pack()

    g1 = GraphCanvas(top, "Граф 1")
    g2 = GraphCanvas(top, "Граф 2")

    bottom = tk.Frame(root)
    bottom.pack(pady=8)

    status = tk.Label(root, text="Нарисуйте два графа и нажмите «Проверить»",
                      fg='blue', wraplength=1000)
    status.pack(pady=5)

    def load_example():
        g1.load(
            {'A': (80, 80), 'B': (300, 80), 'C': (80, 300), 'D': (300, 300)},
            [('A', 'B'), ('A', 'C'), ('B', 'D'), ('C', 'D')]
        )
        g2.load(
            {'P': (80, 80), 'Q': (300, 80), 'R': (80, 300), 'S': (300, 300)},
            [('P', 'Q'), ('P', 'R'), ('R', 'S'), ('Q', 'S')]
        )
        status.config(text="Загружен пример: два изоморфных квадрата. Нажмите «Проверить»",
                      fg='blue')

    def check():
        m1, names1 = g1.get_matrix()
        m2, names2 = g2.get_matrix()

        if len(m1) == 0 or len(m2) == 0:
            status.config(text="Один из графов пуст", fg='red')
            return

        if len(m1) != len(m2):
            status.config(text=f"Разное число вершин: {len(m1)} и {len(m2)} — НЕ изоморфны",
                          fg='red')
            g1.highlight = {}
            g2.highlight = {}
            g1.draw()
            g2.draw()
            return

        result = is_isomorphic(m1, m2)

        g1.highlight = {}
        g2.highlight = {}

        if result is None:
            status.config(text="Графы НЕ изоморфны", fg='red')
        else:
            for i, j in enumerate(result):
                color = PALETTE[i % len(PALETTE)]
                g1.highlight[names1[i]] = color
                g2.highlight[names2[j]] = color
            pairs = ", ".join(f"{names1[i]}={names2[j]}" for i, j in enumerate(result))
            status.config(text=f"Графы ИЗОМОРФНЫ! Соответствие: {pairs}", fg='green')

        g1.draw()
        g2.draw()

    tk.Button(bottom, text="🔍 Проверить изоморфность", bg='lightgreen',
              font=('Arial', 11, 'bold'), command=check).pack(side=tk.LEFT, padx=5)
    tk.Button(bottom, text="📋 Загрузить пример", command=load_example).pack(side=tk.LEFT, padx=5)

    root.mainloop()


if __name__ == '__main__':
    main()