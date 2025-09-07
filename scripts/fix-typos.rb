#!/usr/bin/env ruby

# Script to automatically fix the obvious English typos found in the slides
# This script focuses only on clear, unambiguous typos in English content

class TypoFixer
  def initialize
    # Only the most obvious English typos that are safe to auto-fix
    @fixes = [
      {
        file: 'content/07-ci/_index.md',
        line: 98,
        old: 'This si different',
        new: 'This is different'
      },
      {
        file: 'content/09-containerization/_index.md', 
        line: 1100,
        old: 'docker docker:dind',
        new: 'docker:dind'
      },
      {
        file: 'content/12-multiplatform/_index.md',
        line: 2094, 
        old: 'and the the lack',
        new: 'and the lack'
      },
      {
        file: 'content/extra/pps-ci-introduction/_index.md',
        line: 1206,
        old: 'This si different', 
        new: 'This is different'
      },
      {
        file: 'shared-slides/ci/intro.md',
        line: 54,
        old: 'This si different',
        new: 'This is different'
      }
    ]
  end

  def fix_file(file_path, old_text, new_text)
    return false unless File.exist?(file_path)
    
    content = File.read(file_path, encoding: 'UTF-8')
    original_content = content.dup
    
    # Replace the specific text
    content.gsub!(old_text, new_text)
    
    if content != original_content
      File.write(file_path, content)
      puts "✅ Fixed: #{file_path}"
      puts "   '#{old_text}' → '#{new_text}'"
      return true
    else
      puts "⚠️  No changes made to: #{file_path}"
      puts "   Could not find: '#{old_text}'"
      return false
    end
  end

  def run(dry_run: false)
    puts "#{dry_run ? 'DRY RUN: ' : ''}Fixing obvious English typos..."
    puts

    fixed_count = 0
    
    @fixes.each do |fix|
      file_path = fix[:file]
      old_text = fix[:old] 
      new_text = fix[:new]
      
      puts "Processing: #{file_path}"
      
      if dry_run
        puts "  Would fix: '#{old_text}' → '#{new_text}'"
      else
        if fix_file(file_path, old_text, new_text)
          fixed_count += 1
        end
      end
      puts
    end
    
    if dry_run
      puts "DRY RUN complete. #{@fixes.length} files would be processed."
    else
      puts "✅ Fixed #{fixed_count} out of #{@fixes.length} files."
      
      if fixed_count > 0
        puts "\nRemember to:"
        puts "1. Review the changes with 'git diff'"
        puts "2. Run the build process to ensure everything still works"
        puts "3. Commit the typo fixes"
      end
    end
  end
end

# Parse command line arguments
dry_run = ARGV.include?('--dry-run') || ARGV.include?('-n')

if ARGV.include?('--help') || ARGV.include?('-h')
  puts "Usage: #{$0} [--dry-run|-n] [--help|-h]"
  puts
  puts "Options:"
  puts "  --dry-run, -n    Show what would be fixed without making changes"
  puts "  --help, -h       Show this help message"
  exit 0
end

# Run the fixer
fixer = TypoFixer.new
fixer.run(dry_run: dry_run)