class BookfinderController < ApplicationController
  protect_from_forgery
  
  def index

  end
  
  def search

  end
  
  def find
    if params[:id]
      @response = HTTParty.get("https://www.googleapis.com/books/v1/volumes?q=isbn:" + params[:id])
    else
      @response = HTTParty.get("https://www.googleapis.com/books/v1/volumes?q=isbn:" + params[:isbn])
    end
    
    render json: @response
  end

end
