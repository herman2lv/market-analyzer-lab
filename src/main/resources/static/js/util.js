export function assignMissing(target, source) {
  if (source == null) return target;
  for (const key in source) {
    if (target[key] === void 0) {
      target[key] = source[key];
    }
  }
  return target;
}
