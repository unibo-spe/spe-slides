using System;
using System.Diagnostics;
using Unibo.Spe;

Point first = new Point { X = 1 };
Point copy = first; // struct assignment copies the value
copy.X = 2;
var counter = new Counter();
var alias = counter; // class assignment copies the reference
alias.Value = 2;
object boxed = first; // boxing copies the struct into an object
Console.WriteLine($"{first.X}, {copy.X}"); // 1, 2
Console.WriteLine(counter); // virtual ToString(): Counter(2)

Debug.Assert(first.X == 1 && copy.X == 2);
Debug.Assert(counter.Value == 2 && ReferenceEquals(counter, alias));
Debug.Assert(((Point)boxed).X == 1);
var row = new CsvRow("Ada", "42");
Debug.Assert(row.Count == 2 && string.Join(",", row) == "Ada,42");

public struct Point { public int X; }
public class Counter
{
    public int Value { get; set; }
    public override string ToString() => $"Counter({Value})";
}
