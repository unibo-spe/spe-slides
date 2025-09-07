#!/usr/bin/env ruby

# Simple script to show only definite typos, filtering out suspicious patterns
require_relative 'typo-check'

checker = TypoChecker.new
results = checker.run

puts "\n\n" + "="*60
puts "DEFINITE TYPOS ONLY"
puts "="*60

typos_only = results.select { |r| r[:type] == 'typo' }

if typos_only.empty?
  puts "✅ No definite typos found!"
else
  typos_only.group_by { |r| r[:file] }.each do |file, file_results|
    puts "\n📄 #{file}"
    puts "-" * (file.length + 3)
    
    file_results.each do |result|
      puts "  ❌ Line #{result[:line]}: '#{result[:issue]}' → '#{result[:suggestion]}'"
      puts "     Context: #{result[:content]}"
      puts
    end
  end
  
  puts "\n📊 SUMMARY OF TYPOS"
  puts "Files with typos: #{typos_only.group_by { |r| r[:file] }.keys.length}"
  puts "Total typos: #{typos_only.length}"
end