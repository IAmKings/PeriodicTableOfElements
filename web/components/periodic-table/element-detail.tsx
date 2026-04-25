"use client"

import { Element, categoryInfo } from "@/lib/elements-data"
import { cn } from "@/lib/utils"
import { X } from "lucide-react"
import { ElectronShell } from "./electron-shell"

interface ElementDetailProps {
  element: Element | null
  onClose: () => void
}

export function ElementDetail({ element, onClose }: ElementDetailProps) {
  if (!element) return null

  const category = categoryInfo[element.category]

  return (
    <div 
      className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm animate-in fade-in duration-200 overflow-y-auto"
      onClick={onClose}
    >
      <div 
        className={cn(
          "relative w-full max-w-2xl bg-slate-900/95 border border-white/10 rounded-xl overflow-hidden my-4",
          "shadow-2xl",
          "animate-in zoom-in-95 duration-200"
        )}
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header with category color */}
        <div className={cn("h-2", category.color)} />
        
        {/* Close button */}
        <button 
          onClick={onClose}
          className="absolute top-4 right-4 z-10 p-2 rounded-full bg-white/10 hover:bg-white/20 transition-colors cursor-pointer"
          aria-label="关闭"
        >
          <X className="w-4 h-4 text-white" />
        </button>

        <div className="p-6">
          {/* Main info */}
          <div className="flex items-start gap-6">
            {/* Symbol box */}
            <div className={cn(
              "flex flex-col items-center justify-center w-24 h-24 rounded-lg border border-white/20",
              category.color,
              "shadow-lg"
            )}>
              <span className="text-xs text-white/70 font-mono">{element.atomicNumber}</span>
              <span className="text-4xl font-bold text-white">{element.symbol}</span>
              <span className="text-sm text-white/80">{element.atomicMass.toFixed(3)}</span>
            </div>

            {/* Names and category */}
            <div className="flex-1">
              <h2 className="text-2xl font-bold text-white">{element.nameZh}</h2>
              <h3 className="text-lg text-white/70">{element.nameEn}</h3>
              <div className={cn(
                "inline-block mt-2 px-3 py-1 rounded-full text-xs font-medium text-white",
                category.color
              )}>
                {category.nameZh} / {category.nameEn}
              </div>
            </div>
          </div>

          {/* Description */}
          <p className="mt-4 text-sm text-white/70 leading-relaxed">
            {element.description}
          </p>

          {/* Electron Shell Visualization */}
          <div className="mt-6 p-4 bg-white/5 rounded-lg border border-white/10">
            <h4 className="text-sm font-medium text-white/80 mb-4 text-center">
              电子层分布 <span className="text-white/50">/ Electron Shells</span>
            </h4>
            <ElectronShell element={element} />
          </div>

          {/* Properties grid */}
          <div className="mt-6 grid grid-cols-2 gap-4">
            <PropertyItem 
              label="电子排布" 
              labelEn="Electron Config" 
              value={element.electronConfiguration} 
            />
            <PropertyItem 
              label="电负性" 
              labelEn="Electronegativity" 
              value={element.electronegativity?.toFixed(2) || "N/A"} 
            />
            <PropertyItem 
              label="熔点" 
              labelEn="Melting Point" 
              value={element.meltingPoint !== null ? `${element.meltingPoint}°C` : "N/A"} 
            />
            <PropertyItem 
              label="沸点" 
              labelEn="Boiling Point" 
              value={element.boilingPoint !== null ? `${element.boilingPoint}°C` : "N/A"} 
            />
            <PropertyItem 
              label="密度" 
              labelEn="Density" 
              value={element.density !== null ? `${element.density} g/cm³` : "N/A"} 
            />
            <PropertyItem 
              label="发现年份" 
              labelEn="Discovery" 
              value={element.discoveryYear?.toString() || "史前"} 
            />
            <PropertyItem 
              label="周期" 
              labelEn="Period" 
              value={element.period.toString()} 
            />
            <PropertyItem 
              label="族" 
              labelEn="Group" 
              value={element.group?.toString() || "N/A"} 
            />
          </div>
        </div>
      </div>
    </div>
  )
}

function PropertyItem({ label, labelEn, value }: { label: string; labelEn: string; value: string }) {
  return (
    <div className="bg-white/5 rounded-lg p-3 border border-white/5">
      <div className="text-xs text-white/50 mb-1">
        {label} <span className="text-white/30">/ {labelEn}</span>
      </div>
      <div className="text-sm font-medium text-white font-mono">{value}</div>
    </div>
  )
}
