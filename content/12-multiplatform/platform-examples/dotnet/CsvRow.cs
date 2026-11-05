using System.Collections;
using System.Collections.Generic;

namespace Unibo.Spe;

public class CsvRow : IEnumerable<string>
{
    private readonly string[] _values;

    public CsvRow(params string[] values) // variadic arguments
    {
        _values = (string[])values.Clone();
    }

    public int Count => _values.Length; // property, not GetCount()

    public IEnumerator<string> GetEnumerator() =>
        ((IEnumerable<string>)_values).GetEnumerator();

    IEnumerator IEnumerable.GetEnumerator() => GetEnumerator();
}
