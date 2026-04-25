"use client"

import { Element, categoryInfo } from "@/lib/elements-data"
import { cn } from "@/lib/utils"

interface ElementCardProps {
  element: Element
  isHighlighted?: boolean
  isFiltered?: boolean
  onClick: (element: Element) => void
  cellSize?: number
}

export function ElementCard({ 
  element, 
  isHighlighted = true, 
  isFiltered = true,
  onClick,
  cellSize = 44
}: ElementCardProps) {
  const category = categoryInfo[element.category]

  return (
    <button
      onClick={() => onClick(element)}
      style={{ width: `${cellSize}px`, height: `${cellSize}px` }}
      className={cn(
        "relative flex flex-col items-center justify-center rounded-md border border-white/10 transition-all duration-300",
        category.color,
        isFiltered && isHighlighted 
          ? `hover:scale-110 hover:z-10 hover:shadow-lg cursor-pointer opacity-100` 
          : "opacity-20 cursor-default",
        "group"
      )}
      disabled={!isFiltered || !isHighlighted}
    >
      {/* Glow effect on hover */}
      <div className={cn(
        "absolute inset-0 rounded-md opacity-0 group-hover:opacity-100 transition-opacity duration-300",
        "shadow-[0_0_15px_rgba(255,255,255,0.3)]"
      )} />
      
      {/* Atomic number */}
      <span className="absolute top-0.5 left-1 text-[6px] text-white/70 font-mono">
        {element.atomicNumber}
      </span>
      
      {/* Symbol */}
      <span className="font-bold text-white text-sm leading-none">
        {element.symbol}
      </span>
      
      {/* Chinese name */}
      <span className="text-[8px] text-white/80 leading-none mt-0.5">
        {element.nameZh}
      </span>
    </button>
  )
}
