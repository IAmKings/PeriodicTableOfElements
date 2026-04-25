"use client"

import { useState, useEffect, useCallback } from "react"
import { elements, Element, categoryInfo } from "@/lib/elements-data"
import { cn } from "@/lib/utils"
import { Trophy, RefreshCw, CheckCircle, XCircle, Zap, Target, PartyPopper, Star } from "lucide-react"

const TOTAL_QUIZ_QUESTIONS = 5

type QuizType = "symbol" | "name" | "category" | "atomicNumber"

interface Question {
  element: Element
  type: QuizType
  options: string[]
  correctAnswer: string
}

export function QuizMode() {
  const [currentQuestion, setCurrentQuestion] = useState<Question | null>(null)
  const [score, setScore] = useState(0)
  const [totalQuestions, setTotalQuestions] = useState(0)
  const [selectedAnswer, setSelectedAnswer] = useState<string | null>(null)
  const [showResult, setShowResult] = useState(false)
  const [streak, setStreak] = useState(0)

  const generateQuestion = useCallback((): Question => {
    const element = elements[Math.floor(Math.random() * elements.length)]
    const types: QuizType[] = ["symbol", "name", "category", "atomicNumber"]
    const type = types[Math.floor(Math.random() * types.length)]
    
    let options: string[] = []
    let correctAnswer: string = ""

    switch (type) {
      case "symbol":
        correctAnswer = element.symbol
        options = [element.symbol]
        while (options.length < 4) {
          const randomElement = elements[Math.floor(Math.random() * elements.length)]
          if (!options.includes(randomElement.symbol)) {
            options.push(randomElement.symbol)
          }
        }
        break
      case "name":
        correctAnswer = element.nameZh
        options = [element.nameZh]
        while (options.length < 4) {
          const randomElement = elements[Math.floor(Math.random() * elements.length)]
          if (!options.includes(randomElement.nameZh)) {
            options.push(randomElement.nameZh)
          }
        }
        break
      case "category":
        correctAnswer = categoryInfo[element.category].nameZh
        options = Object.values(categoryInfo).map(c => c.nameZh)
        options = options.sort(() => Math.random() - 0.5).slice(0, 4)
        if (!options.includes(correctAnswer)) {
          options[0] = correctAnswer
        }
        break
      case "atomicNumber":
        correctAnswer = element.atomicNumber.toString()
        options = [correctAnswer]
        while (options.length < 4) {
          const randomNum = Math.floor(Math.random() * 118) + 1
          if (!options.includes(randomNum.toString())) {
            options.push(randomNum.toString())
          }
        }
        break
    }

    return {
      element,
      type,
      options: options.sort(() => Math.random() - 0.5),
      correctAnswer
    }
  }, [])

  useEffect(() => {
    setCurrentQuestion(generateQuestion())
  }, [generateQuestion])

  const handleAnswer = (answer: string) => {
    setSelectedAnswer(answer)
    setShowResult(true)
    setTotalQuestions(prev => prev + 1)
    
    if (answer === currentQuestion?.correctAnswer) {
      setScore(prev => prev + 1)
      setStreak(prev => prev + 1)
    } else {
      setStreak(0)
    }
  }

  const nextQuestion = () => {
    setSelectedAnswer(null)
    setShowResult(false)
    setCurrentQuestion(generateQuestion())
  }

  const resetQuiz = () => {
    setScore(0)
    setTotalQuestions(0)
    setStreak(0)
    setSelectedAnswer(null)
    setShowResult(false)
    setCurrentQuestion(generateQuestion())
  }

  if (!currentQuestion) return null

  const getQuestionText = () => {
    switch (currentQuestion.type) {
      case "symbol":
        return `"${currentQuestion.element.nameZh}" 的元素符号是什么？`
      case "name":
        return `元素符号 "${currentQuestion.element.symbol}" 代表哪个元素？`
      case "category":
        return `"${currentQuestion.element.nameZh} (${currentQuestion.element.symbol})" 属于哪一类？`
      case "atomicNumber":
        return `"${currentQuestion.element.nameZh} (${currentQuestion.element.symbol})" 的原子序数是多少？`
    }
  }

  const category = categoryInfo[currentQuestion.element.category]

  return (
    <div className="w-full max-w-2xl mx-auto p-6 rounded-xl bg-slate-900/80 border border-white/10 backdrop-blur-sm">
      {/* Header with score */}
      <div className="flex items-center justify-between mb-6">
        <div className="flex items-center gap-4">
          <div className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-yellow-500/20 border border-yellow-500/30">
            <Trophy className="w-4 h-4 text-yellow-400" />
            <span className="text-yellow-400 font-mono">{score}/{totalQuestions}</span>
          </div>
          {streak > 1 && (
            <div className="flex items-center gap-1 px-3 py-1.5 rounded-lg bg-orange-500/20 border border-orange-500/30 animate-pulse">
              <Zap className="w-4 h-4 text-orange-400" />
              <span className="text-orange-400 text-sm font-mono">{streak} 连击!</span>
            </div>
          )}
        </div>
        <button
          onClick={resetQuiz}
          className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-white/5 border border-white/10 text-white/70 hover:bg-white/10 transition-colors"
        >
          <RefreshCw className="w-4 h-4" />
          <span className="text-sm">重新开始</span>
        </button>
      </div>

      {/* Element display */}
      <div className="flex justify-center mb-6">
        <div className={cn(
          "w-24 h-24 rounded-lg border border-white/20 flex flex-col items-center justify-center",
          category.color,
          "shadow-lg"
        )}>
          <span className="text-xs text-white/70 font-mono">{currentQuestion.element.atomicNumber}</span>
          <span className="text-3xl font-bold text-white">{currentQuestion.element.symbol}</span>
          <span className="text-sm text-white/80">{currentQuestion.element.nameZh}</span>
        </div>
      </div>

      {/* Question */}
      <div className="text-center mb-6">
        <div className="flex items-center justify-center gap-2 mb-2">
          <Target className="w-4 h-4 text-cyan-400" />
          <span className="text-xs text-cyan-400/70 uppercase tracking-wider">问题</span>
        </div>
        <h3 className="text-lg text-white font-medium">{getQuestionText()}</h3>
      </div>

      {/* Options */}
      <div className="grid grid-cols-2 gap-3 mb-6">
        {currentQuestion.options.map((option, index) => {
          const isCorrect = option === currentQuestion.correctAnswer
          const isSelected = option === selectedAnswer
          
          return (
            <button
              key={index}
              onClick={() => !showResult && handleAnswer(option)}
              disabled={showResult}
              className={cn(
                "p-4 rounded-lg border text-center transition-all duration-200",
                showResult
                  ? isCorrect
                    ? "bg-green-500/20 border-green-500/50 text-green-400"
                    : isSelected
                      ? "bg-red-500/20 border-red-500/50 text-red-400"
                      : "bg-white/5 border-white/10 text-white/50"
                  : "bg-white/5 border-white/10 text-white hover:bg-white/10 hover:border-white/20"
              )}
            >
              <div className="flex items-center justify-center gap-2">
                {showResult && isCorrect && <CheckCircle className="w-4 h-4" />}
                {showResult && isSelected && !isCorrect && <XCircle className="w-4 h-4" />}
                <span className="font-medium">{option}</span>
              </div>
            </button>
          )
        })}
      </div>

      {/* Result feedback */}
      {showResult && (
        <div className="text-center">
          <div className={cn(
            "inline-flex items-center gap-2 px-4 py-2 rounded-full mb-4",
            selectedAnswer === currentQuestion.correctAnswer
              ? "bg-green-500/20 text-green-400"
              : "bg-red-500/20 text-red-400"
          )}>
            {selectedAnswer === currentQuestion.correctAnswer ? (
              <>
                <CheckCircle className="w-5 h-5" />
                <span>正确!</span>
              </>
            ) : (
              <>
                <XCircle className="w-5 h-5" />
                <span>错误! 正确答案是: {currentQuestion.correctAnswer}</span>
              </>
            )}
          </div>
          <button
            onClick={nextQuestion}
            className="block w-full py-3 rounded-lg bg-cyan-500/20 border border-cyan-500/50 text-cyan-400 hover:bg-cyan-500/30 transition-colors"
          >
            下一题
          </button>
        </div>
      )}
    </div>
  )
}
