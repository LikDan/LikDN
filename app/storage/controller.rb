# frozen_string_literal: true

class Controller
  def store_file(file)
    extension = file[:filename].partition('.').last
    filename = (0..5).map { SecureRandom.hex }.join

    path = "storage/#{filename}.#{extension}"
    File.open(path, 'wb') {|f| f.write file[:tempfile].read }

    path
  end

  def get_file(filename)
    File.expand_path("storage/#{filename}")
  end
end
