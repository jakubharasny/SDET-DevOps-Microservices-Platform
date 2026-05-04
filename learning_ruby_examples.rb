# Ruby quick reference aligned with learning_java_examples.java

# 01) VARIABLES + CONSTANTS
name = "Jakub"                  # mutable local variable
age = 29                        # integer
price = 19.99                   # float
active = true                   # boolean
NOTES_LIMIT = 100               # constant

# 02) STRINGS
full = "#{name} is #{age}"      # interpolation
caps = full.upcase              # string method
trimmed = "  hi  ".strip        # trim spaces

# 03) ARRAYS
nums = [1, 2, 3, 4]             # array literal
first = nums.first              # first element
evens = nums.select(&:even?)    # filter with block

# 04) HASHES
user = { id: 1, role: "admin" } # symbol keys
role = user[:role]              # read value
user[:active] = active          # write value

# 05) CONDITIONALS
label = age >= 18 ? "adult" : "minor" # ternary
if active && age > 20           # if with boolean logic
  status = "allowed"
else
  status = "blocked"
end

# 06) CASE (SWITCH STYLE)
access = case role              # case expression
         when "admin" then "all"
         when "user" then "limited"
         else "none"
         end
has_full_access = access == "all" # use declaration result

# 07) LOOPS
sum = 0
nums.each { |n| sum += n }      # each loop
for n in nums                   # for loop
  break if n > 3                # break
end

# 08) RANGES
range = (1..5)                  # inclusive range
letters = ("a".."c").to_a       # range to array

# 09) METHODS
def greet(person)               # method definition
  "Hello #{person}"
end
message = greet(name)           # call method
name_length = message.length    # call String method

# 10) DEFAULT + KEYWORD ARGUMENTS
def build_tag(prefix = "dev", env: "local")
  "#{prefix}-#{env}"
end
tag = build_tag(env: "test")    # keyword argument
tag_parts = tag.split("-")      # call method on returned value

# 11) SPLAT ARGUMENTS
def total(*values)              # variable arg count
  values.sum
end
total_sum = total(1, 2, 3, 4)
many_values = total_sum > 5     # use method call result

# 12) BLOCKS + LAMBDAS + PROCS
def with_timing
  start = Time.now
  result = yield                # execute passed block
  [result, Time.now - start]
end
double = ->(x) { x * 2 }        # lambda literal
triple = Proc.new { |x| x * 3 } # proc object
timed = with_timing { double.call(10) }
tripled = triple.call(4)        # call proc object

# 13) CLASSES + OBJECTS
class Person
  attr_accessor :name           # getter + setter
  attr_reader :created_at       # getter only
  @@count = 0                   # class variable

  def initialize(name)
    @name = name                # instance variable
    @created_at = Time.now
    @@count += 1
  end

  def self.count                # class method
    @@count
  end

  def rename!(new_name)
    @name = new_name
  end
end
person = Person.new("Mia")
person.rename!("Mila")          # call instance method

# 14) INHERITANCE
class Admin < Person
  def role
    "admin"
  end
end
admin = Admin.new("Root")
admin_role = admin.role         # call subclass method

# 15) MODULES (MIXINS)
module Loggable
  def log(msg)
    "[LOG] #{msg}"
  end
end
class Worker
  include Loggable              # mix in module methods
end
worker_log = Worker.new.log("job started")

# 16) VISIBILITY
class Vault
  def expose
    secret_code                 # private method call
  end

  private

  def secret_code
    "42"
  end
end
secret = Vault.new.expose

# 17) EXCEPTIONS
begin
  Integer("oops")               # raises ArgumentError
rescue ArgumentError => e
  handled = e.message
ensure
  finalized = true              # always runs
end

# 18) FILE I/O
path = "tmp_ruby_example.txt"
File.write(path, "hello\n")     # write text file
content = File.read(path)       # read text file
File.delete(path) if File.exist?(path)

# 19) REGEX
has_digits = "abc123".match?(/\d+/) # regex match
replaced = "2026-05-04".sub(/\d{4}/, "YEAR")

# 20) ENUMERABLE CHAINING
squares = nums.map { |n| n * n }    # transform
small = squares.reject { |n| n > 10 } # reject
joined = small.join(",")            # array to string

# 21) NIL-SAFE HELPERS
nickname = nil
safe_length = nickname&.length      # safe navigation
fallback = nickname || "anonymous"  # default fallback

# 22) SYMBOLS + CONVERSIONS
sym = :status                        # symbol literal
sym_text = sym.to_s                  # symbol to string
num_text = 42.to_s                   # number to string
to_num = "42".to_i                   # string to number

# 23) ENTRY OUTPUT
puts message
puts "tag=#{tag} role=#{access} count=#{Person.count}"
puts "worker=#{worker_log} secret=#{secret} content=#{content.strip}"
