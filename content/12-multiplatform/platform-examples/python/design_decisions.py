class Row:
    def __init__(self, *values):
        self.values = values

    def __str__(self):
        return ",".join(self.values)

    def __len__(self):
        return len(self.values)


row = Row("Ada", "42")
print(str(row))  # Ada,42: dispatches to __str__
print(len(row))  # 2: dispatches to __len__
print(isinstance(42, object))  # True, even for integers

assert str(row) == "Ada,42"
assert len(row) == 2
assert isinstance(42, object)
