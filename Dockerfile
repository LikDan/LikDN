FROM ruby:2.7.4

WORKDIR .
COPY . .
RUN bundle install

CMD ["bundle", "exec", "rackup", "--host", "0.0.0.0"]