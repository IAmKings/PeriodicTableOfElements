"use client"

import { useState } from "react"
import { categoryInfo, ElementCategory } from "@/lib/elements-data"
import { cn } from "@/lib/utils"
import { Search, Filter, X } from "lucide-react"

interface SearchFilterProps {
  searchQuery: string
  onSearchChange: (query: string) => void
  selectedCategories: Set<ElementCategory>
  onCategoriesChange: (categories: Set<ElementCategory>) => void
}

export function SearchFilter({
  searchQuery,
  onSearchChange,
  selectedCategories,
  onCategoriesChange
}: SearchFilterProps) {
  const [showFilters, setShowFilters] = useState(false)

  return (
    <div className="w-full space-y-4">
      {/* Search bar */}
      <div className="flex gap-2">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-white/40" />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => onSearchChange(e.target.value)}
            placeholder="搜索元素 (符号、名称、原子序数)..."
            className={cn(
              "w-full pl-10 pr-4 py-3 rounded-lg",
              "bg-white/5 border border-white/10",
              "text-white placeholder:text-white/40",
              "focus:outline-none focus:border-cyan-500/50 focus:bg-white/10",
              "transition-all duration-200"
            )}
          />
          {searchQuery && (
            <button
              onClick={() => onSearchChange("")}
              className="absolute right-3 top-1/2 -translate-y-1/2 p-1 rounded-full hover:bg-white/10"
            >
              <X className="w-3 h-3 text-white/40" />
            </button>
          )}
        </div>
        <button
          onClick={() => setShowFilters(!showFilters)}
          className={cn(
            "px-4 py-3 rounded-lg border transition-all duration-200",
            "flex items-center gap-2",
            showFilters 
              ? "bg-cyan-500/20 border-cyan-500/50 text-cyan-400" 
              : "bg-white/5 border-white/10 text-white/70 hover:bg-white/10"
          )}
        >
          <Filter className="w-4 h-4" />
          <span className="hidden sm:inline">筛选</span>
        </button>
      </div>

      {/* Category filters */}
      {showFilters && (
        <div className="p-4 rounded-lg bg-white/5 border border-white/10 animate-in slide-in-from-top-2 duration-200">
          <div className="flex items-center justify-between mb-3">
            <span className="text-sm text-white/70">
              按类别筛选 
              {selectedCategories.size > 0 && (
                <span className="ml-2 text-cyan-400">({selectedCategories.size} 个已选)</span>
              )}
            </span>
            {selectedCategories.size > 0 && (
              <button
                onClick={() => onCategoriesChange(new Set())}
                className="text-xs text-cyan-400 hover:text-cyan-300"
              >
                清除筛选
              </button>
            )}
          </div>
          <div className="flex flex-wrap gap-2">
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
                  className={cn(
                    "flex items-center gap-2 px-3 py-2 rounded-lg border transition-all duration-200",
                    isSelected
                      ? `${info.color} border-white/30 text-white shadow-lg`
                      : "bg-white/5 border-white/10 text-white/70 hover:bg-white/10"
                  )}
                >
                  <div className={cn("w-2.5 h-2.5 rounded-sm", info.color)} />
                  <span className="text-xs">{info.nameZh}</span>
                </button>
              )
            })}
          </div>
        </div>
      )}
    </div>
  )
}
