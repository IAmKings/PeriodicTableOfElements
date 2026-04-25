"use client"

import { useState, useMemo } from "react"
import { elements, Element, categoryInfo } from "@/lib/elements-data"
import { cn } from "@/lib/utils"
import { BarChart3, TrendingUp, Atom } from "lucide-react"

type PropertyKey = "atomicMass" | "electronegativity" | "meltingPoint" | "boilingPoint" | "density"

const propertyLabels: Record<PropertyKey, { zh: string; en: string; unit: string }> = {
  atomicMass: { zh: "原子质量", en: "Atomic Mass", unit: "u" },
  electronegativity: { zh: "电负性", en: "Electronegativity", unit: "" },
  meltingPoint: { zh: "熔点", en: "Melting Point", unit: "°C" },
  boilingPoint: { zh: "沸点", en: "Boiling Point", unit: "°C" },
  density: { zh: "密度", en: "Density", unit: "g/cm³" },
}

export function DataVisualization({ onHighlight }: { onHighlight: (elements: Set<number>) => void }) {
  const [selectedProperty, setSelectedProperty] = useState<PropertyKey>("electronegativity")
  const [comparisonMode, setComparisonMode] = useState<"trend" | "top10">("trend")

  const validElements = useMemo(() => {
    return elements.filter(e => {
      const value = e[selectedProperty]
      return value !== null && value !== undefined && !isNaN(value as number)
    })
  }, [selectedProperty])

  const sortedElements = useMemo(() => {
    return [...validElements].sort((a, b) => {
      const aVal = a[selectedProperty] as number
      const bVal = b[selectedProperty] as number
      return bVal - aVal
    })
  }, [validElements, selectedProperty])

  const top10 = sortedElements.slice(0, 10)
  const bottom10 = sortedElements.slice(-10).reverse()

  const maxValue = sortedElements.length > 0 ? (sortedElements[0][selectedProperty] as number) : 0

  // Get elements by period for trend view
  const elementsByPeriod = useMemo(() => {
    const periods: Record<number, Element[]> = {}
    validElements.forEach(el => {
      if (!periods[el.period]) periods[el.period] = []
      periods[el.period].push(el)
    })
    // Sort elements in each period by group
    Object.keys(periods).forEach(p => {
      periods[parseInt(p)].sort((a, b) => (a.group || 0) - (b.group || 0))
    })
    return periods
  }, [validElements])

  const handleHoverElement = (element: Element | null) => {
    if (element) {
      onHighlight(new Set([element.atomicNumber]))
    } else {
      onHighlight(new Set())
    }
  }

  return (
    <div className="w-full max-w-4xl mx-auto p-6 rounded-xl bg-slate-900/80 border border-white/10 backdrop-blur-sm">
      {/* Header */}
      <div className="flex items-center gap-3 mb-6">
        <div className="p-2 rounded-lg bg-cyan-500/20">
          <BarChart3 className="w-5 h-5 text-cyan-400" />
        </div>
        <div>
          <h2 className="text-lg font-semibold text-white">数据可视化</h2>
          <p className="text-sm text-white/50">比较元素属性和趋势</p>
        </div>
      </div>

      {/* Property selector */}
      <div className="flex flex-wrap gap-2 mb-4">
        {Object.entries(propertyLabels).map(([key, label]) => (
          <button
            key={key}
            onClick={() => setSelectedProperty(key as PropertyKey)}
            className={cn(
              "px-3 py-2 rounded-lg border text-sm transition-all duration-200",
              selectedProperty === key
                ? "bg-cyan-500/20 border-cyan-500/50 text-cyan-400"
                : "bg-white/5 border-white/10 text-white/70 hover:bg-white/10"
            )}
          >
            {label.zh}
          </button>
        ))}
      </div>

      {/* Mode selector */}
      <div className="flex gap-2 mb-6">
        <button
          onClick={() => setComparisonMode("top10")}
          className={cn(
            "flex items-center gap-2 px-3 py-2 rounded-lg border text-sm transition-all duration-200",
            comparisonMode === "top10"
              ? "bg-cyan-500/20 border-cyan-500/50 text-cyan-400"
              : "bg-white/5 border-white/10 text-white/70 hover:bg-white/10"
          )}
        >
          <Atom className="w-4 h-4" />
          前10 / 后10
        </button>
        <button
          onClick={() => setComparisonMode("trend")}
          className={cn(
            "flex items-center gap-2 px-3 py-2 rounded-lg border text-sm transition-all duration-200",
            comparisonMode === "trend"
              ? "bg-cyan-500/20 border-cyan-500/50 text-cyan-400"
              : "bg-white/5 border-white/10 text-white/70 hover:bg-white/10"
          )}
        >
          <TrendingUp className="w-4 h-4" />
          周期趋势
        </button>
      </div>

      {/* Visualization area */}
      {comparisonMode === "top10" ? (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Top 10 */}
          <div>
            <h3 className="text-sm text-white/70 mb-3">
              {propertyLabels[selectedProperty].zh} 最高的10个元素
            </h3>
            <div className="space-y-2">
              {top10.map((element, index) => {
                const value = element[selectedProperty] as number
                const percentage = (value / maxValue) * 100
                const category = categoryInfo[element.category]
                
                return (
                  <div 
                    key={element.atomicNumber}
                    className="flex items-center gap-3 group cursor-pointer"
                    onMouseEnter={() => handleHoverElement(element)}
                    onMouseLeave={() => handleHoverElement(null)}
                  >
                    <span className="w-6 text-xs text-white/40 font-mono">{index + 1}</span>
                    <div className={cn(
                      "w-10 h-10 rounded-md flex items-center justify-center text-sm font-bold text-white border border-white/20",
                      category.color,
                      "group-hover:scale-110 transition-transform"
                    )}>
                      {element.symbol}
                    </div>
                    <div className="flex-1">
                      <div className="flex items-center justify-between mb-1">
                        <span className="text-sm text-white">{element.nameZh}</span>
                        <span className="text-xs text-white/70 font-mono">
                          {value.toFixed(2)} {propertyLabels[selectedProperty].unit}
                        </span>
                      </div>
                      <div className="h-1.5 bg-white/10 rounded-full overflow-hidden">
                        <div 
                          className={cn("h-full rounded-full", category.color)}
                          style={{ width: `${percentage}%` }}
                        />
                      </div>
                    </div>
                  </div>
                )
              })}
            </div>
          </div>

          {/* Bottom 10 */}
          <div>
            <h3 className="text-sm text-white/70 mb-3">
              {propertyLabels[selectedProperty].zh} 最低的10个元素
            </h3>
            <div className="space-y-2">
              {bottom10.map((element, index) => {
                const value = element[selectedProperty] as number
                const percentage = (value / maxValue) * 100
                const category = categoryInfo[element.category]
                
                return (
                  <div 
                    key={element.atomicNumber}
                    className="flex items-center gap-3 group cursor-pointer"
                    onMouseEnter={() => handleHoverElement(element)}
                    onMouseLeave={() => handleHoverElement(null)}
                  >
                    <span className="w-6 text-xs text-white/40 font-mono">{sortedElements.length - 9 + index}</span>
                    <div className={cn(
                      "w-10 h-10 rounded-md flex items-center justify-center text-sm font-bold text-white border border-white/20",
                      category.color,
                      "group-hover:scale-110 transition-transform"
                    )}>
                      {element.symbol}
                    </div>
                    <div className="flex-1">
                      <div className="flex items-center justify-between mb-1">
                        <span className="text-sm text-white">{element.nameZh}</span>
                        <span className="text-xs text-white/70 font-mono">
                          {value.toFixed(2)} {propertyLabels[selectedProperty].unit}
                        </span>
                      </div>
                      <div className="h-1.5 bg-white/10 rounded-full overflow-hidden">
                        <div 
                          className={cn("h-full rounded-full", category.color)}
                          style={{ width: `${Math.max(percentage, 2)}%` }}
                        />
                      </div>
                    </div>
                  </div>
                )
              })}
            </div>
          </div>
        </div>
      ) : (
        /* Trend view */
        <div className="space-y-4">
          <h3 className="text-sm text-white/70">
            {propertyLabels[selectedProperty].zh} 随周期变化趋势
          </h3>
          <div className="overflow-x-auto">
            <div className="min-w-[600px]">
              {[1, 2, 3, 4, 5, 6, 7].map(period => {
                const periodElements = elementsByPeriod[period] || []
                if (periodElements.length === 0) return null
                
                return (
                  <div key={period} className="flex items-end gap-1 mb-2">
                    <span className="w-8 text-xs text-white/40 font-mono shrink-0">P{period}</span>
                    <div className="flex items-end gap-0.5 flex-1">
                      {periodElements.map(element => {
                        const value = element[selectedProperty] as number
                        const normalizedHeight = (value / maxValue) * 100
                        const category = categoryInfo[element.category]
                        
                        return (
                          <div
                            key={element.atomicNumber}
                            className="flex flex-col items-center group cursor-pointer"
                            onMouseEnter={() => handleHoverElement(element)}
                            onMouseLeave={() => handleHoverElement(null)}
                          >
                            <div 
                              className={cn(
                                "w-6 rounded-t border border-white/20 transition-all",
                                category.color,
                                "group-hover:opacity-100 opacity-80"
                              )}
                              style={{ height: `${Math.max(normalizedHeight, 4)}px` }}
                            />
                            <span className="text-[8px] text-white/50 mt-0.5 group-hover:text-white transition-colors">
                              {element.symbol}
                            </span>
                          </div>
                        )
                      })}
                    </div>
                  </div>
                )
              })}
            </div>
          </div>
          <div className="text-xs text-white/40 text-center">
            高度表示 {propertyLabels[selectedProperty].zh} 相对值
          </div>
        </div>
      )}
    </div>
  )
}
