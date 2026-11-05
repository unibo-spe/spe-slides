import assert from 'node:assert/strict';

export class CsvRow { // PascalCase type name
  constructor(...values) { // rest arguments: Array
    this.values = values;
  }

  [Symbol.iterator]() { // iterable protocol
    return this.values[Symbol.iterator]();
  }
}

function joinValues(row) { // camelCase; duck typing
  return [...row].join(','); // any iterable
}

const row = new CsvRow('Ada', '42');
console.log(joinValues(row)); // Ada,42
console.log(joinValues(['Ada', '42'])); // same protocol

assert.equal(joinValues(row), 'Ada,42');
assert.equal(joinValues(['Ada', '42']), 'Ada,42');
