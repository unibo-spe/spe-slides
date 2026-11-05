class CsvRow:  # PascalCase type name
    def __init__(self, *values, **metadata):
        self.values = values       # tuple
        self.metadata = metadata   # dict

    def __iter__(self):
        return iter(self.values)


def join_values(row):  # snake_case; duck typing
    return ",".join(row)  # any iterable of strings


row = CsvRow("Ada", "42", source="demo")
print(join_values(row))  # Ada,42
print(join_values(["Ada", "42"]))  # same protocol

assert join_values(row) == join_values(["Ada", "42"])
assert row.values == ("Ada", "42")
assert row.metadata == {"source": "demo"}
