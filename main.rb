require 'sinatra'
require 'securerandom'

server_url = ENV['SERVER_URL']

post '/storage' do
  tempfile = params[:file][:tempfile]

  extension = params[:file][:filename].partition('.').last
  filename = (0..5).map { SecureRandom.hex }.join

  path = "storage/#{filename}.#{extension}"

  File.open(path, 'wb') {|f| f.write tempfile.read }

  "#{server_url}#{path}"
end

get '/storage/:path' do
  path = params[:path]

  send_file File.expand_path("storage/#{path}")
end