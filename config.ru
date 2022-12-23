require './app/storage/handler'

run Rack::Cascade.new [Handler]
