#!/usr/bin/env ruby

# Script to search for common typos across all slide content
# Searches through content/, reusable/, and shared-slides/ directories

require 'find'

class TypoChecker
  def initialize
    # Common typos and their corrections
    @typo_patterns = {
      # Common English typos
      /\bteh\b/i => 'the',
      /\bsi\b(?=\s+[a-z])/i => 'is', # si followed by a word (likely 'si' instead of 'is')
      /\bwiht\b/i => 'with',
      /\bform\b(?=\s+(a|the|an)\s+)/i => 'from', # 'form' when it should be 'from'
      /\bof\s+of\b/i => 'of',
      /\bthe\s+the\b/i => 'the',
      /\band\s+and\b/i => 'and',
      /\bto\s+to\b/i => 'to',
      /\bis\s+is\b/i => 'is',
      /\bthat\s+that\b/i => 'that',
      /\bin\s+in\b/i => 'in',
      /\bfor\s+for\b/i => 'for',
      /\bwith\s+with\b/i => 'with',
      /\bthier\b/i => 'their',
      /\brecieve\b/i => 'receive',
      /\boccured\b/i => 'occurred',
      /\baccommodate\b/i => 'accommodate',
      /\bdefinate\b/i => 'definite',
      /\bseperate\b/i => 'separate',
      /\bmispelled\b/i => 'misspelled',
      /\bagain\s+and\s+again/i => 'again and again',
      
      # Technical/programming typos
      /\bfuntion\b/i => 'function',
      /\bparamter\b/i => 'parameter',
      /\bparamters\b/i => 'parameters',
      /\bimplementation\s+implementation\b/i => 'implementation',
      /\bdevelopment\s+development\b/i => 'development',
      /\bapplication\s+application\b/i => 'application',
      /\bconfiguratoin\b/i => 'configuration',
      /\barchitecture\s+architecture\b/i => 'architecture',
      /\bframwork\b/i => 'framework',
      /\blibaray\b/i => 'library',
      /\blibraries\s+libraries\b/i => 'libraries',
      /\brepository\s+repository\b/i => 'repository',
      /\bdeployment\s+deployment\b/i => 'deployment',
      /\bcontainer\s+container\b/i => 'container',
      /\bautomation\s+automation\b/i => 'automation',
      /\bintegration\s+integration\b/i => 'integration',
      
      # DevOps specific typos
      /\bCI\/CD\s+CI\/CD\b/i => 'CI/CD',
      /\bkubernetes\s+kubernetes\b/i => 'kubernetes',
      /\bdocker\s+docker\b/i => 'docker',
      /\bpipeline\s+pipeline\b/i => 'pipeline',
      /\bworkflow\s+workflow\b/i => 'workflow',
    }
    
    @suspicious_patterns = [
      # Words that are commonly misspelled
      /\b\w*[qx][^u]\w*\b/i, # words with q/x not followed by u (often typos)
      /\b\w*\d+[a-z]+\b/i,   # mixed numbers and letters (might be formatting issues)
      /\s{3,}/,              # excessive spaces
      /[.]{3,}/,             # excessive dots (should probably be ...)
    ]
  end

  def scan_directory(dir)
    results = []
    
    Find.find(dir) do |path|
      next unless File.file?(path) && path.end_with?('.md')
      
      results.concat(scan_file(path))
    end
    
    results
  end

  def scan_file(file_path)
    return [] unless File.exist?(file_path)
    
    content = File.read(file_path, encoding: 'UTF-8')
    results = []
    
    content.lines.each_with_index do |line, line_num|
      # Skip markdown code blocks and URLs
      next if line.strip.start_with?('```', 'http', '![', '<', '<!--')
      next if line.strip.empty?
      
      @typo_patterns.each do |pattern, correction|
        if match = line.match(pattern)
          results << {
            file: file_path,
            line: line_num + 1,
            content: line.strip,
            issue: match.to_s,
            suggestion: correction,
            type: 'typo'
          }
        end
      end
      
      @suspicious_patterns.each do |pattern|
        if match = line.match(pattern)
          results << {
            file: file_path,
            line: line_num + 1,
            content: line.strip,
            issue: match.to_s,
            suggestion: 'Review for potential typo',
            type: 'suspicious'
          }
        end
      end
    end
    
    results
  end

  def generate_report(results)
    if results.empty?
      puts "✅ No common typos found!"
      return
    end

    puts "🔍 Typo Check Report"
    puts "=" * 50
    puts

    # Group by file
    results.group_by { |r| r[:file] }.each do |file, file_results|
      puts "📄 #{file}"
      puts "-" * (file.length + 3)
      
      file_results.each do |result|
        icon = result[:type] == 'typo' ? '❌' : '⚠️'
        puts "  #{icon} Line #{result[:line]}: #{result[:issue]}"
        puts "     Suggestion: #{result[:suggestion]}"
        puts "     Context: #{result[:content][0, 80]}#{'...' if result[:content].length > 80}"
        puts
      end
      puts
    end

    # Summary
    typos = results.select { |r| r[:type] == 'typo' }
    suspicious = results.select { |r| r[:type] == 'suspicious' }
    
    puts "📊 Summary"
    puts "=" * 20
    puts "Files scanned: #{results.group_by { |r| r[:file] }.keys.length}"
    puts "Definite typos: #{typos.length}"
    puts "Suspicious patterns: #{suspicious.length}"
    puts "Total issues: #{results.length}"
  end

  def run
    puts "Starting typo check across all slide content..."
    puts

    all_results = []
    
    # Check content directory
    if Dir.exist?('content')
      puts "Scanning content/ directory..."
      all_results.concat(scan_directory('content'))
    end
    
    # Check reusable directory
    if Dir.exist?('reusable')
      puts "Scanning reusable/ directory..."
      all_results.concat(scan_directory('reusable'))
    end
    
    # Check shared-slides directory
    if Dir.exist?('shared-slides')
      puts "Scanning shared-slides/ directory..."
      all_results.concat(scan_directory('shared-slides'))
    end
    
    puts "Scan complete!"
    puts
    
    generate_report(all_results)
    
    # Return results for potential further processing
    all_results
  end
end

# Run the checker if this script is executed directly
if __FILE__ == $0
  checker = TypoChecker.new
  checker.run
end