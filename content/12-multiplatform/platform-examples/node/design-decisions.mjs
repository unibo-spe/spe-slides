import assert from 'node:assert/strict';

const rowPrototype = {
  toString() { return this.values.join(','); }
};
const row = Object.create(rowPrototype);
row.values = ['Ada', '42'];
console.log(row.toString()); // inherited: Ada,42

const order = [];
async function readRow() {
  order.push('start');
  await Promise.resolve(row); // continuation runs later
  order.push('resumed');
}
const pending = readRow();
order.push('caller'); // await did not block the caller
await pending;
console.log(order); // ['start', 'caller', 'resumed']

assert.equal(row.toString(), 'Ada,42');
assert.deepEqual(order, ['start', 'caller', 'resumed']);
