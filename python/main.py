from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity
from typing import List

class PurchaseHistory(BaseModel):
    customer_id: int
    product_id: int
    total_quantity: int

class PurchaseHistoryList(BaseModel):
    purchase_history: List[PurchaseHistory]

app = FastAPI()

@app.post("/recommend/{id}")
def recommend(id: int, data: PurchaseHistoryList):
    print(f"Received data: {data}")
    # JSON 데이터를 pandas DataFrame으로 변환 (속성명 맞추기)
    df = pd.DataFrame([{"customer_id": p.customer_id, "product_id": p.product_id, "quantity": p.total_quantity} for p in data.purchase_history])

    # DataFrame 확인
    print(f"DataFrame:\n{df}")

    # 피벗 테이블 생성 (CUSTOMER_ID x PRODUCT_ID, 값: QUANTITY)
    try:
        pivot = df.pivot_table(index='customer_id', columns='product_id', values='quantity', fill_value=0)
        print(f"Pivot table:\n{pivot}")
    except KeyError as e:
        print(f"KeyError: {e}")
        return {"error": f"Missing key: {e}"}

    # 고객 간 유사도 계산
    customer_similarity = cosine_similarity(pivot)
    similarity_df = pd.DataFrame(customer_similarity, index=pivot.index, columns=pivot.index)
    
    # 추천 로직
    def get_recommendations(id):
        # 현재 고객과 유사한 고객 리스트
        similar_customers = similarity_df[id].sort_values(ascending=False).index[1:]

        # 유사 고객이 구매한 상품 중 현재 고객이 구매하지 않은 상품
        customer_products = set(df[df['customer_id'] == id]['product_id'])
        similar_products = df[df['customer_id'].isin(similar_customers)]['product_id']
        recommendations = [p for p in similar_products if p not in customer_products]

        # 추천 상품 상위 5개
        return pd.Series(recommendations).value_counts().head(5).index.tolist()

    # 고객 ID가 유효한지 확인하고 추천 결과 생성
    try:
        recommendations = get_recommendations(id)
        print(id, recommendations)
        return {"customer_id": id, "recommended_products": recommendations}
    except KeyError:
        raise HTTPException(status_code=404, detail="Customer ID not found")

if __name__ == "__main__":
    import uvicorn

    # FastAPI 앱 실행
    uvicorn.run(app, host="0.0.0.0", port=8000)