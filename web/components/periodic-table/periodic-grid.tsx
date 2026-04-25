"use client"

import { Element, elements, categoryInfo, ElementCategory } from "@/lib/elements-data"
import { ElementCard } from "./element-card"

interface PeriodicGridProps {
  selectedCategories: Set<ElementCategory>
  searchQuery: string
  highlightedElements: Set<number>
  onElementClick: (element: Element) => void
  onCategoriesChange: (categories: Set<ElementCategory>) => void
}

export function PeriodicGrid({ 
  selectedCategories, 
  searchQuery,
  highlightedElements,
  onElementClick,
  onCategoriesChange
}: PeriodicGridProps) {
  const isElementHighlighted = (element: Element) => {
    if (highlightedElements.size > 0) {
      return highlightedElements.has(element.atomicNumber)
    }
    if (selectedCategories.size > 0) {
      return selectedCategories.has(element.category)
    }
    if (searchQuery) {
      const q = searchQuery.toLowerCase()
      return (
        element.symbol.toLowerCase().includes(q) ||
        element.nameEn.toLowerCase().includes(q) ||
        element.nameZh.includes(q) ||
        element.atomicNumber.toString() === q
      )
    }
    return true
  }

  // Main table elements (excluding lanthanides and actinides)
  const mainElements = elements.filter(e => e.category !== "lanthanide" && e.category !== "actinide")
  const lanthanides = elements.filter(e => e.category === "lanthanide")
  const actinides = elements.filter(e => e.category === "actinide")

  // Create a 2D grid representation (7 periods x 18 groups)
  const grid: (Element | null)[][] = Array(7).fill(null).map(() => Array(18).fill(null))
  
  mainElements.forEach(element => {
    if (element.group && element.period) {
      grid[element.period - 1][element.group - 1] = element
    }
  })

  // Cell size in pixels (fixed for proper alignment)
  const cellSize = 44
  const gap = 2

  return (
    <div className="w-full overflow-x-auto pb-4">
      <div 
        className="mx-auto"
        style={{ 
          width: `${(cellSize + gap) * 19 + 24}px`,
          minWidth: `${(cellSize + gap) * 19 + 24}px`
        }}
      >
        {/* Group numbers header */}
        <div 
          className="grid mb-1"
          style={{ 
            gridTemplateColumns: `24px repeat(18, ${cellSize}px)`,
            gap: `${gap}px`
          }}
        >
          <div /> {/* Empty corner */}
          {Array(18).fill(null).map((_, i) => (
            <div 
              key={i} 
              className="text-center text-white/40 text-[10px] font-mono"
              style={{ height: '16px' }}
            >
              {i + 1}
            </div>
          ))}
        </div>

        {/* Main table grid */}
        {grid.map((row, rowIndex) => (
          <div 
            key={rowIndex}
            className="grid"
            style={{ 
              gridTemplateColumns: `24px repeat(18, ${cellSize}px)`,
              gap: `${gap}px`,
              marginBottom: `${gap}px`
            }}
          >
            {/* Period number */}
            <div 
              className="flex items-center justify-center text-white/40 text-[10px] font-mono"
              style={{ height: `${cellSize}px` }}
            >
              {rowIndex + 1}
            </div>
            
            {/* Elements in this period */}
            {row.map((element, colIndex) => (
              <div 
                key={colIndex}
                style={{ width: `${cellSize}px`, height: `${cellSize}px` }}
              >
                {element ? (
                  <ElementCard
                    element={element}
                    isHighlighted={isElementHighlighted(element)}
                    onClick={onElementClick}
                    cellSize={cellSize}
                  />
                ) : (
                  // Placeholder markers for lanthanides/actinides
                  rowIndex === 5 && colIndex === 2 ? (
                    <div 
                      className="w-full h-full flex items-center justify-center text-[9px] text-pink-400/60 font-mono border border-dashed border-pink-400/30 rounded"
                    >
                      57-71
                    </div>
                  ) : rowIndex === 6 && colIndex === 2 ? (
                    <div 
                      className="w-full h-full flex items-center justify-center text-[9px] text-rose-400/60 font-mono border border-dashed border-rose-400/30 rounded"
                    >
                      89-103
                    </div>
                  ) : null
                )}
              </div>
            ))}
          </div>
        ))}

        {/* Spacer */}
        <div className="h-4" />

        {/* Lanthanides row */}
        <div 
          className="grid"
          style={{ 
            gridTemplateColumns: `24px repeat(18, ${cellSize}px)`,
            gap: `${gap}px`,
            marginBottom: `${gap}px`
          }}
        >
          <div 
            className="flex items-center justify-center text-pink-400/60 text-[10px] font-mono"
            style={{ height: `${cellSize}px` }}
          >
            *
          </div>
          {/* Empty cells before lanthanides (2 cells) */}
          <div style={{ width: `${cellSize}px`, height: `${cellSize}px` }} />
          <div style={{ width: `${cellSize}px`, height: `${cellSize}px` }} />
          {/* Lanthanides (15 elements) */}
          {lanthanides.map(element => (
            <div 
              key={element.atomicNumber}
              style={{ width: `${cellSize}px`, height: `${cellSize}px` }}
            >
              <ElementCard
                element={element}
                isHighlighted={isElementHighlighted(element)}
                onClick={onElementClick}
                cellSize={cellSize}
              />
            </div>
          ))}
          {/* Fill remaining cells if needed */}
          {Array(18 - 2 - lanthanides.length).fill(null).map((_, i) => (
            <div key={`la-empty-${i}`} style={{ width: `${cellSize}px`, height: `${cellSize}px` }} />
          ))}
        </div>

        {/* Actinides row */}
        <div 
          className="grid"
          style={{ 
            gridTemplateColumns: `24px repeat(18, ${cellSize}px)`,
            gap: `${gap}px`,
            marginBottom: `${gap}px`
          }}
        >
          <div 
            className="flex items-center justify-center text-rose-400/60 text-[10px] font-mono"
            style={{ height: `${cellSize}px` }}
          >
            **
          </div>
          {/* Empty cells before actinides (2 cells) */}
          <div style={{ width: `${cellSize}px`, height: `${cellSize}px` }} />
          <div style={{ width: `${cellSize}px`, height: `${cellSize}px` }} />
          {/* Actinides (15 elements) */}
          {actinides.map(element => (
            <div 
              key={element.atomicNumber}
              style={{ width: `${cellSize}px`, height: `${cellSize}px` }}
            >
              <ElementCard
                element={element}
                isHighlighted={isElementHighlighted(element)}
                onClick={onElementClick}
                cellSize={cellSize}
              />
            </div>
          ))}
          {/* Fill remaining cells if needed */}
          {Array(18 - 2 - actinides.length).fill(null).map((_, i) => (
            <div key={`ac-empty-${i}`} style={{ width: `${cellSize}px`, height: `${cellSize}px` }} />
          ))}
        </div>

        {/* Category legend */}
        <div className="mt-6 flex flex-wrap gap-2 justify-center px-4">
          {Object.entries(categoryInfo).map(([key, info]) => {
            const category = key as ElementCategory
            const isSelected = selectedCategories.has(category)
            return (
              <button 
                key={key}
                onClick={() => {
                  const newSet = new Set(selectedCategories)
                  if (isSelected) {
                    newSet.delete(category)
                  } else {
                    newSet.add(category)
                  }
                  onCategoriesChange(newSet)
                }}
                className={`flex items-center gap-1.5 px-2 py-1 rounded-full border transition-all duration-200 cursor-pointer ${
                  isSelected 
                    ? 'bg-white/20 border-white/40 ring-1 ring-white/20' 
                    : 'bg-white/5 border-white/10 hover:bg-white/10 hover:border-white/20'
                }`}
              >
                <div className={`w-2.5 h-2.5 rounded-sm ${info.color}`} />
                <span className={`text-[10px] ${isSelected ? 'text-white' : 'text-white/70'}`}>
                  {info.nameZh}
                </span>
              </button>
            )
          })}
        </div>
      </div>
    </div>
  )
}
