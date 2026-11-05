using System;
using System.Diagnostics;
using CsvDemo.Models;

namespace CsvDemo;

internal static class Program
{
    public static void Main()
    {
        Point first = new Point { X = 1 };
        Point copy = first; // struct: copy the value
        copy.X = 2;
        var counter = new Counter();
        var alias = counter; // class: copy the reference
        alias.Value = 2;
        object boxed = first; // box a copy of the struct
        Console.WriteLine($"{first.X}, {copy.X}"); // 1, 2
        Console.WriteLine(counter); // Counter(2)

        Debug.Assert(first.X == 1 && copy.X == 2);
        Debug.Assert(ReferenceEquals(counter, alias) && counter.Value == 2);
        Debug.Assert(((Point)boxed).X == 1);
        var row = new CsvRow("Ada", "42");
        Debug.Assert(row.Count == 2 && string.Join(",", row) == "Ada,42");
    }
}
