type DotPosition = {
  top: string;
  left: string;
};

export const isOverlap = (newDot: DotPosition, existingDots: DotPosition[]) => {
  for (const existingDot of existingDots) {
    const dx = parseFloat(newDot.left) - parseFloat(existingDot.left);
    const dy = parseFloat(newDot.top) - parseFloat(existingDot.top);
    const distance = Math.sqrt(dx * dx + dy * dy);
    if (distance < 5) {
      return true;
    }
  }
  return false;
};
