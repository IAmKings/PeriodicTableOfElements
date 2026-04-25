"use client"

import { useState } from "react"
import { Element, ElementCategory } from "@/lib/elements-data"
import { PeriodicGrid } from "@/components/periodic-table/periodic-grid"
import { ElementDetail } from "@/components/periodic-table/element-detail"
import { SearchFilter } from "@/components/periodic-table/search-filter"
import { QuizMode } from "@/components/periodic-table/quiz-mode"
import { DataVisualization } from "@/components/periodic-table/data-visualization"
import { cn } from "@/lib/utils"
import { Atom, BookOpen, BarChart3, Sparkles } from "lucide-react"

type ViewMode = "table" | "quiz" | "visualization"

export default function PeriodicTableApp() {
  const [selectedElement, setSelectedElement] = useState<Element | null>(null)
  const [searchQuery, setSearchQuery] = useState("")
  const [selectedCategories, setSelectedCategories] = useState<Set<ElementCategory>>(new Set())
  const [viewMode, setViewMode] = useState<ViewMode>("table")
  const [highlightedElements, setHighlightedElements] = useState<Set<number>>(new Set())

  const handleElementClick = (element: Element) => {
    setSelectedElement(element)
  }

  return (
    <div className="min-h-screen bg-slate-950 text-white overflow-x-hidden">
      {/* Animated background */}
      <div className="fixed inset-0 overflow-hidden pointer-events-none">
        <div className="absolute top-1/4 -left-1/4 w-1/2 h-1/2 bg-cyan-500/10 rounded-full blur-3xl animate-pulse" />
        <div className="absolute bottom-1/4 -right-1/4 w-1/2 h-1/2 bg-blue-500/10 rounded-full blur-3xl animate-pulse delay-1000" />
        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-1/3 h-1/3 bg-indigo-500/5 rounded-full blur-3xl animate-pulse delay-500" />
        {/* Grid overlay */}
        <div 
          className="absolute inset-0 opacity-[0.02]"
          style={{
            backgroundImage: `
              linear-gradient(rgba(255,255,255,0.1) 1px, transparent 1px),
              linear-gradient(90deg, rgba(255,255,255,0.1) 1px, transparent 1px)
            `,
            backgroundSize: '50px 50px'
          }}
        />
      </div>

      {/* Header */}
      <header className="relative z-10 border-b border-white/5">
        <div className="max-w-7xl mx-auto px-4 py-4 sm:py-6">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
            <div className="flex items-center gap-3">
              <div className="relative">
                <div className="absolute inset-0 bg-cyan-500 blur-lg opacity-50" />
                <div className="relative p-2 rounded-xl bg-gradient-to-br from-cyan-400 to-blue-500">
                  <Atom className="w-6 h-6 text-white" />
                </div>
              </div>
              <div>
                <h1 className="text-xl sm:text-2xl font-bold bg-gradient-to-r from-white to-white/70 bg-clip-text text-transparent">
                  元素周期表
                </h1>
                <p className="text-xs text-white/50">Periodic Table of Elements</p>
              </div>
            </div>

            {/* View mode tabs */}
            <div className="flex gap-2 p-1 rounded-lg bg-white/5 border border-white/10">
              <button
                onClick={() => setViewMode("table")}
                className={cn(
                  "flex items-center gap-2 px-4 py-2 rounded-md text-sm transition-all duration-200",
                  viewMode === "table"
                    ? "bg-cyan-500/20 text-cyan-400 shadow-lg shadow-cyan-500/20"
                    : "text-white/60 hover:text-white hover:bg-white/5"
                )}
              >
                <Sparkles className="w-4 h-4" />
                <span className="hidden sm:inline">周期表</span>
              </button>
              <button
                onClick={() => setViewMode("quiz")}
                className={cn(
                  "flex items-center gap-2 px-4 py-2 rounded-md text-sm transition-all duration-200",
                  viewMode === "quiz"
                    ? "bg-cyan-500/20 text-cyan-400 shadow-lg shadow-cyan-500/20"
                    : "text-white/60 hover:text-white hover:bg-white/5"
                )}
              >
                <BookOpen className="w-4 h-4" />
                <span className="hidden sm:inline">测验</span>
              </button>
              <button
                onClick={() => setViewMode("visualization")}
                className={cn(
                  "flex items-center gap-2 px-4 py-2 rounded-md text-sm transition-all duration-200",
                  viewMode === "visualization"
                    ? "bg-cyan-500/20 text-cyan-400 shadow-lg shadow-cyan-500/20"
                    : "text-white/60 hover:text-white hover:bg-white/5"
                )}
              >
                <BarChart3 className="w-4 h-4" />
                <span className="hidden sm:inline">可视化</span>
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Main content */}
      <main className="relative z-10 max-w-7xl mx-auto px-4 py-6">
        {viewMode === "table" && (
          <>
            {/* Search and filter */}
            <div className="mb-6">
              <SearchFilter
                searchQuery={searchQuery}
                onSearchChange={setSearchQuery}
                selectedCategories={selectedCategories}
                onCategoriesChange={setSelectedCategories}
              />
            </div>

            {/* Periodic table */}
            <PeriodicGrid
              selectedCategories={selectedCategories}
              searchQuery={searchQuery}
              highlightedElements={highlightedElements}
              onElementClick={handleElementClick}
              onCategoriesChange={setSelectedCategories}
            />
          </>
        )}

        {viewMode === "quiz" && <QuizMode />}

        {viewMode === "visualization" && (
          <DataVisualization onHighlight={setHighlightedElements} />
        )}
      </main>

      {/* Element detail modal */}
      <ElementDetail
        element={selectedElement}
        onClose={() => setSelectedElement(null)}
      />

      {/* Footer */}
      <footer className="relative z-10 border-t border-white/5 mt-12">
        <div className="max-w-7xl mx-auto px-4 py-6 text-center text-xs text-white/30">
          <p>元素周期表 | 118 个元素 | 中英双语</p>
        </div>
      </footer>
    </div>
  )
}
