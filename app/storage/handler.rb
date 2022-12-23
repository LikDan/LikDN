# frozen_string_literal: true
require 'sinatra'
require 'securerandom'
require './app/storage/controller'

class Handler < Sinatra::Base
  attr_reader :controller, :server_url

  def initialize(app = nil, **_kwargs)
    @controller = Controller.new
    @server_url = ENV['SERVER_URL']
    super
  end

  post '/storage' do
    file = params[:file]
    path = @controller.store_file file

    "#{@server_url}#{path}"
  end

  get '/storage/:path' do
    path = params[:path]

    send_file @controller.get_file path
  end
end
